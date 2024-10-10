package com.seocho507.couponcore.repository.redis

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.seocho507.couponcore.entity.Coupon
import com.seocho507.couponcore.entity.CouponType
import com.seocho507.couponcore.exception.CouponIssueException
import java.time.LocalDateTime

data class CouponRedisEntity(
    val id: Long,
    val couponType: CouponType,
    var totalQuantity: Int?,
    val availableIssueQuantity: Boolean,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val dateIssueStart: LocalDateTime,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val dateIssueEnd: LocalDateTime
) {

    constructor(coupon: Coupon) : this(
        coupon.id,
        coupon.couponType,
        coupon.totalQuantity,
        coupon.availableIssueQuantity(),
        coupon.dateIssueStart,
        coupon.dateIssueEnd
    )

    private fun availableIssueDate(): Boolean {
        val now = LocalDateTime.now()
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now)
    }

    fun checkIssuableCoupon() {
        if (!availableIssueQuantity) {
            throw CouponIssueException.FailCouponIssueException()
        }
        if (!availableIssueDate()) {
            throw CouponIssueException.FailCouponIssueException()
        }
    }
}