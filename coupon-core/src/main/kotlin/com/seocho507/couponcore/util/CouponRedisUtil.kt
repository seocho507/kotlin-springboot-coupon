package com.seocho507.couponcore.util

object CouponRedisUtils {

    fun getIssueRequestKey(couponId: Long): String {
        return "issue.request.couponId=$couponId"
    }

    fun getIssueRequestQueueKey(): String {
        return "issue.request"
    }
}