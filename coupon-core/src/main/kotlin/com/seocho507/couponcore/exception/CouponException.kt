package com.seocho507.couponcore.exception

sealed class CouponException(
    val errorCode: CouponErrorCode,
    message: String
) : RuntimeException(message) {

    class CouponNotExistException(couponId: Long) :
        CouponException(
            CouponErrorCode.COUPON_NOT_EXIST,
            "존재하지 않는 쿠폰입니다. couponId : $couponId"
        )
}