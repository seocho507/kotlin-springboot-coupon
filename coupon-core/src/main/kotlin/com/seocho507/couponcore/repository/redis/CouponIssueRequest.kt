package com.seocho507.couponcore.repository.redis

data class CouponIssueRequest(
    val couponId: Long,
    val userId: Long
)
