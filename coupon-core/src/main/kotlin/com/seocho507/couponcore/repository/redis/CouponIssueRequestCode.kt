package com.seocho507.couponcore.repository.redis

import com.seocho507.couponcore.exception.CouponIssueException

enum class CouponIssueRequestCode(val code: Int) {
    SUCCESS(1),
    DUPLICATED_COUPON_ISSUE(2),
    INVALID_COUPON_ISSUE_QUANTITY(3);

    companion object {
        fun find(code: String): CouponIssueRequestCode {
            val codeValue = code.toInt()
            return when (codeValue) {
                1 -> SUCCESS
                2 -> DUPLICATED_COUPON_ISSUE
                3 -> INVALID_COUPON_ISSUE_QUANTITY
                else -> throw IllegalArgumentException("존재하지 않는 코드입니다. $code")
            }
        }

        fun checkRequestResult(code: CouponIssueRequestCode) {
            when (code) {
                INVALID_COUPON_ISSUE_QUANTITY -> throw CouponIssueException.FailCouponIssueException()
                DUPLICATED_COUPON_ISSUE -> throw CouponIssueException.FailCouponIssueException()
                else -> Unit
            }
        }
    }
}