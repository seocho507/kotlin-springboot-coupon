package com.seocho507.couponcore.exception

import java.time.LocalDateTime

sealed class CouponIssueException(
    val errorCode: CouponErrorCode,
    message: String
) : RuntimeException(message) {

    class InvalidQuantityException(totalQuantity: Int?, issuedQuantity: Int) :
        CouponIssueException(
            CouponErrorCode.INVALID_COUPON_ISSUE_QUANTITY,
            "발급 가능한 수량을 초과했습니다. total : $totalQuantity, issued : $issuedQuantity"
        )

    class InvalidDateException(requestDate: LocalDateTime, issueStart: LocalDateTime, issueEnd: LocalDateTime) :
        CouponIssueException(
            CouponErrorCode.INVALID_COUPON_ISSUE_DATE,
            "발급 가능한 일자가 아닙니다. request : $requestDate, issueStart : $issueStart, issueEnd : $issueEnd"
        )

    class DuplicatedCouponIssueException(couponId: Long, userId: Long) :
        CouponIssueException(
            CouponErrorCode.DUPLICATED_COUPON_ISSUE,
            "이미 발급된 쿠폰입니다. couponId : $couponId, userId : $userId"
        )

    class FailCouponIssueRequestException(couponId: Long, userId: Long) :
        CouponIssueException(
            CouponErrorCode.FAIL_COUPON_ISSUE_REQUEST,
            "쿠폰 발급 요청에 실패했습니다. couponId : $couponId, userId : $userId"
        )
}