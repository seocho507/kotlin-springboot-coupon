package com.seocho507.couponcore.entity

import com.seocho507.couponcore.exception.CouponIssueException
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupons")
class Coupon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    val couponType: CouponType,

    val totalQuantity: Int?,

    @Column(nullable = false)
    var issuedQuantity: Int = 0,

    @Column(nullable = false)
    val discountAmount: Int,

    @Column(nullable = false)
    val minAvailableAmount: Int,

    @Column(nullable = false)
    val dateIssueStart: LocalDateTime,

    @Column(nullable = false)
    val dateIssueEnd: LocalDateTime
) : BaseTimeEntity() {
    fun availableIssueQuantity(): Boolean {
        return totalQuantity?.let { it > issuedQuantity } ?: true
    }

    fun availableIssueDate(): Boolean {
        val now = LocalDateTime.now()
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now)
    }

    fun isIssueComplete(): Boolean {
        val now = LocalDateTime.now()
        return dateIssueEnd.isBefore(now) || !availableIssueQuantity()
    }

    fun issue() {
        if (!availableIssueQuantity()) {
            throw CouponIssueException.InvalidQuantityException(totalQuantity, issuedQuantity)
        }

        if (!availableIssueDate()) {
            throw CouponIssueException.InvalidDateException(LocalDateTime.now(), dateIssueStart, dateIssueEnd)
        }

        issuedQuantity++
    }
}
