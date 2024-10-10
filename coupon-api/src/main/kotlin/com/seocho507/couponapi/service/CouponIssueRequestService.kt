package com.seocho507.couponapi.service

interface CouponIssueRequestService {
    fun issueRequest(couponId: Long, userId: Long)
}
