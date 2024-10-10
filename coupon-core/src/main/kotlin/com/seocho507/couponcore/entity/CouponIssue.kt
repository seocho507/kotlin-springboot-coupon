package com.seocho507.couponcore.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_issues")
class CouponIssue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val couponId: Long,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false, updatable = false)
    val dateIssued: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    val dateUsed: LocalDateTime? = null
) : BaseTimeEntity() {

}