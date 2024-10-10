package com.seocho507.couponcore.service

import com.seocho507.couponcore.entity.Coupon
import com.seocho507.couponcore.entity.CouponIssue
import com.seocho507.couponcore.event.CouponIssueCompleteEvent
import com.seocho507.couponcore.exception.CouponException
import com.seocho507.couponcore.exception.CouponIssueException
import com.seocho507.couponcore.repository.CouponIssueJpaRepository
import com.seocho507.couponcore.repository.CouponIssueRepository
import com.seocho507.couponcore.repository.CouponJpaRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.annotation.Transactional

open class CouponIssueServiceImpl(
    private val couponJpaRepository: CouponJpaRepository,
    private val couponIssueJpaRepository: CouponIssueJpaRepository,
    private val couponIssueRepository: CouponIssueRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) : CouponIssueService {

    @Transactional
    override fun issueCoupon(
        couponId: Long,
        userId: Long
    ) {
        val coupon = findCouponByIdWithLock(couponId).apply { issue() }
        saveCouponIssue(couponId, userId)
        publishCouponEventIfComplete(coupon)
    }

    private fun findCouponByIdWithLock(couponId: Long): Coupon =
        couponJpaRepository.findCouponByIdWithLock(couponId)
            ?: throw CouponException.CouponNotExistException(couponId)

    private fun saveCouponIssue(couponId: Long, userId: Long): CouponIssue {
        ensureNoDuplicateIssue(couponId, userId)
        return couponIssueJpaRepository.save(
            CouponIssue(
                couponId = couponId,
                userId = userId
            )
        )
    }

    private fun ensureNoDuplicateIssue(couponId: Long, userId: Long) {
        couponIssueRepository.findFirstCouponIssue(couponId, userId)?.let {
            throw CouponIssueException.DuplicatedCouponIssueException(couponId, userId)
        }
    }

    private fun publishCouponEventIfComplete(coupon: Coupon) {
        if (coupon.isIssueComplete()) {
            applicationEventPublisher.publishEvent(CouponIssueCompleteEvent(coupon.id))
        }
    }
}
