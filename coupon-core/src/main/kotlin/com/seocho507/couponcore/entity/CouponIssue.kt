package com.seocho507.couponcore.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_issues")
class CouponIssue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val couponId: Long,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val dateIssued: LocalDateTime,

    @Column(nullable = false)
    val dateUsed: LocalDateTime
) : BaseTimeEntity() {

}