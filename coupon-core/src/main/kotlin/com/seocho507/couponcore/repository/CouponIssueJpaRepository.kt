package com.seocho507.couponcore.repository

import com.seocho507.couponcore.entity.CouponIssue
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssueJpaRepository : JpaRepository<CouponIssue, Long> {
}