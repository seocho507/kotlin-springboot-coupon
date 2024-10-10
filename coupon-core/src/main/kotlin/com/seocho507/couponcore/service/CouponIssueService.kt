package com.seocho507.couponcore.service

interface CouponIssueService {
    fun issueCoupon(couponId: Long, userId: Long)
}