package com.seocho507.couponcore.service

import com.seocho507.couponcore.entity.Coupon
import com.seocho507.couponcore.entity.CouponIssue
import com.seocho507.couponcore.entity.CouponType
import com.seocho507.couponcore.event.CouponIssueCompleteEvent
import com.seocho507.couponcore.exception.CouponException
import com.seocho507.couponcore.exception.CouponIssueException
import com.seocho507.couponcore.repository.CouponIssueJpaRepository
import com.seocho507.couponcore.repository.CouponIssueRepository
import com.seocho507.couponcore.repository.CouponJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class CouponIssueServiceImplTest {

    @Mock
    private lateinit var couponJpaRepository: CouponJpaRepository

    @Mock
    private lateinit var couponIssueJpaRepository: CouponIssueJpaRepository

    @Mock
    private lateinit var couponIssueRepository: CouponIssueRepository

    @Mock
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMocks
    private lateinit var couponIssueService: CouponIssueServiceImpl

    @Test
    fun `쿠폰 발급 시 유효한 couponId와 userId가 주어지면 발급이 성공해야 한다`() {
        // given
        val couponId = 1L
        val userId = 123L
        val coupon = Coupon(
            id = couponId,
            couponType = CouponType.FIRST_COME_FIRST_SERVED,
            totalQuantity = 100, // 총 수량
            issuedQuantity = 50, // 현재 발급된 수량 (발급 가능한 상태)
            discountAmount = 5000,
            minAvailableAmount = 10000,
            dateIssueStart = LocalDateTime.now().minusDays(1), // 발급 가능한 기간
            dateIssueEnd = LocalDateTime.now().plusDays(1)
        )
        val couponIssue = CouponIssue(couponId = couponId, userId = userId)

        // stubbing: 쿠폰과 쿠폰 발급에 대한 동작 설정
        given(couponJpaRepository.findCouponByIdWithLock(couponId)).willReturn(coupon)
        given(couponIssueRepository.findFirstCouponIssue(couponId, userId)).willReturn(null)
        given(couponIssueJpaRepository.save(any(CouponIssue::class.java))).willReturn(couponIssue)

        // when
        couponIssueService.issueCoupon(couponId, userId)

        // then
        then(couponJpaRepository).should().findCouponByIdWithLock(couponId)
        then(couponIssueJpaRepository).should().save(any(CouponIssue::class.java))

        // 발급 수량이 1 증가했는지 확인
        assertThat(coupon.issuedQuantity).isEqualTo(51)
    }

    @Test
    fun `이미 발급된 쿠폰일 때 중복 발급 예외를 발생시켜야 한다`() {
        // given
        val couponId = 1L
        val userId = 123L
        val couponIssue = CouponIssue(couponId = couponId, userId = userId)

        given(couponJpaRepository.findCouponByIdWithLock(couponId)).willReturn(
            Coupon(
                id = couponId,
                couponType = CouponType.FIRST_COME_FIRST_SERVED,
                totalQuantity = 100,
                issuedQuantity = 50,
                discountAmount = 5000,
                minAvailableAmount = 10000,
                dateIssueStart = LocalDateTime.now().minusDays(1),
                dateIssueEnd = LocalDateTime.now().plusDays(1)
            )
        )
        given(couponIssueRepository.findFirstCouponIssue(couponId, userId)).willReturn(couponIssue)

        // when // then
        val exception = catchThrowable {
            couponIssueService.issueCoupon(couponId, userId)
        }

        assertThat(exception)
            .isInstanceOf(CouponIssueException.DuplicatedCouponIssueException::class.java)
            .hasMessageContaining("이미 발급된 쿠폰입니다. couponId : $couponId, userId : $userId")
    }

    @Test
    fun `존재하지 않는 쿠폰일 때 예외를 발생시켜야 한다`() {
        // given
        val couponId = 1L
        val userId = 123L

        given(couponJpaRepository.findCouponByIdWithLock(couponId)).willReturn(null)

        // when / then
        val exception = catchThrowable {
            couponIssueService.issueCoupon(couponId, userId)
        }

        assertThat(exception)
            .isInstanceOf(CouponException.CouponNotExistException::class.java)
            .hasMessageContaining("존재하지 않는 쿠폰입니다. couponId: $couponId")
    }

    @Test
    fun `쿠폰 발급이 완료된 경우 예외가 발생해야 하고 이벤트가 발행되지 않아야 한다`() {
        // given
        val couponId = 1L
        val userId = 123L
        val coupon = Coupon(
            id = couponId,
            couponType = CouponType.FIRST_COME_FIRST_SERVED,
            totalQuantity = 100,
            issuedQuantity = 100, // 발급이 완료된 상태
            discountAmount = 5000,
            minAvailableAmount = 10000,
            dateIssueStart = LocalDateTime.now().minusDays(1),
            dateIssueEnd = LocalDateTime.now().plusDays(1)
        )

        given(couponJpaRepository.findCouponByIdWithLock(couponId)).willReturn(coupon)

        // when
        val exception = catchThrowable { couponIssueService.issueCoupon(couponId, userId) }

        // then
        assertThat(exception).isInstanceOf(CouponIssueException.InvalidQuantityException::class.java)
        then(applicationEventPublisher).should(never()).publishEvent(any(CouponIssueCompleteEvent::class.java))
        assertThat(coupon.issuedQuantity).isEqualTo(100) // 발급 수량이 그대로여야 함
    }
}