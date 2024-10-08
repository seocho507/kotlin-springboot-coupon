package com.seocho507.couponcore.entity

import com.seocho507.couponcore.exception.CouponIssueException
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CouponTest {

    private fun createCoupon(
        totalQuantity: Int? = 100,
        issuedQuantity: Int = 0,
        dateIssueStart: LocalDateTime = LocalDateTime.now().minusDays(1),
        dateIssueEnd: LocalDateTime = LocalDateTime.now().plusDays(1)
    ): Coupon {
        return Coupon(
            id = 1L,
            couponType = CouponType.FIRST_COME_FIRST_SERVED,
            totalQuantity = totalQuantity,
            issuedQuantity = issuedQuantity,
            discountAmount = 5000,
            minAvailableAmount = 10000,
            dateIssueStart = dateIssueStart,
            dateIssueEnd = dateIssueEnd
        )
    }

    @Test
    fun `쿠폰 발급 가능 수량이 남아 있는 경우 발급이 성공해야 한다`() {
        // given
        val coupon = createCoupon(totalQuantity = 100, issuedQuantity = 50)

        // when & then
        assertThatCode {
            coupon.issue()
        }.doesNotThrowAnyException()

        assertThat(coupon.issuedQuantity).isEqualTo(51) // 발급 성공 후 수량이 1 증가 확인
    }

    @Test
    fun `쿠폰 발급 가능 수량이 부족하면 예외가 발생해야 한다`() {
        // given
        val coupon = createCoupon(totalQuantity = 100, issuedQuantity = 100)

        // when
        val exception = catchThrowable { coupon.issue() }

        // then
        assertThat(exception)
            .isInstanceOf(CouponIssueException.InvalidQuantityException::class.java)
            .hasMessage("발급 가능한 수량을 초과했습니다. total : 100, issued : 100")
    }

    @Test
    fun `쿠폰 발급 기간 내에 있으면 발급이 성공해야 한다`() {
        // given
        val coupon = createCoupon(
            dateIssueStart = LocalDateTime.now().minusDays(1),
            dateIssueEnd = LocalDateTime.now().plusDays(1)
        )

        // when & then
        assertThatCode {
            coupon.issue()
        }.doesNotThrowAnyException()
    }

    @Test
    fun `쿠폰 발급 기간이 지난 경우 예외가 발생해야 한다`() {
        // given
        val coupon = createCoupon(
            dateIssueStart = LocalDateTime.now().minusDays(10),
            dateIssueEnd = LocalDateTime.now().minusDays(1)
        )

        // when
        val exception = catchThrowable { coupon.issue() }

        // then
        assertThat(exception)
            .isInstanceOf(CouponIssueException.InvalidDateException::class.java)
            .hasMessage("발급 가능한 일자가 아닙니다. request : ${coupon.createdAt}, issueStart : ${coupon.dateIssueStart}, issueEnd : ${coupon.dateIssueEnd}")
    }

    @Test
    fun `발급 완료된 쿠폰은 더 이상 발급할 수 없어야 한다`() {
        // given
        val coupon = createCoupon(
            totalQuantity = 100,
            issuedQuantity = 100,
            dateIssueStart = LocalDateTime.now().minusDays(1),
            dateIssueEnd = LocalDateTime.now().minusDays(1)
        )

        // when & then
        assertThat(coupon.isIssueComplete()).isTrue() // 발급이 완료된 상태여야 함
    }

    @Test
    fun `발급 완료되지 않은 쿠폰은 발급할 수 있어야 한다`() {
        // given
        val coupon = createCoupon(
            totalQuantity = 100,
            issuedQuantity = 50,
            dateIssueStart = LocalDateTime.now().minusDays(1),
            dateIssueEnd = LocalDateTime.now().plusDays(1)
        )

        // when & then
        assertThat(coupon.isIssueComplete()).isFalse() // 발급 완료되지 않았으므로 false
    }
}
