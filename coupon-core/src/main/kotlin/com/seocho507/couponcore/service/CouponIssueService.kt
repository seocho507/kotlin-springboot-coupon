package com.seocho507.couponcore.service

import com.seocho507.couponcore.entity.Coupon

interface CouponIssueService {
    fun issueCoupon(couponId: Long, userId: Long)
    fun findCoupon(couponId: Long): Coupon
}