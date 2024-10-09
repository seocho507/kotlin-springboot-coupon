package com.seocho507.couponcore.entity

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

    val totalQuantity: Int,

    @Column(nullable = false)
    val issuedQuantity: Int,

    @Column(nullable = false)
    val discountAmount: Int,

    @Column(nullable = false)
    val minAvailableAmount: Int,

    @Column(nullable = false)
    val dateIssueStart: LocalDateTime,

    @Column(nullable = false)
    val dateIssueEnd: LocalDateTime
) : BaseTimeEntity() {

}
