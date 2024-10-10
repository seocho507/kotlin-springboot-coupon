package com.seocho507.couponapi.service

import com.seocho507.couponcore.service.AsyncCouponIssueService
import com.seocho507.couponcore.service.CouponIssueService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CouponIssueRequestServiceImpl(
    private val couponIssueService: CouponIssueService,
    private val asyncCouponIssueService: AsyncCouponIssueService
) : CouponIssueRequestService {

    private val logger: Logger = LoggerFactory.getLogger(CouponIssueRequestServiceImpl::class.java)

    override fun issueRequest(couponId: Long, userId: Long) {
        couponIssueService.issueCoupon(couponId, userId)
        logger.info("Coupon issued: couponId=$couponId, userId=$userId")
    }

    override fun issueRequestAsync(couponId: Long, userId: Long) {
        asyncCouponIssueService.issueCoupon(couponId, userId)
    }
}
