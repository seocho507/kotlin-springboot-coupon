package com.seocho507.couponcore.repository

import com.querydsl.jpa.JPQLQueryFactory
import com.seocho507.couponcore.entity.CouponIssue
import com.seocho507.couponcore.entity.QCouponIssue.couponIssue
import org.springframework.stereotype.Repository

@Repository
class CouponIssueRepository(
    private val queryFactory: JPQLQueryFactory
) {
    fun findFirstCouponIssue(couponId: Long, userId: Long): CouponIssue? {
        return queryFactory.selectFrom(couponIssue)
            .where(couponIssue.couponId.eq(couponId))
            .where(couponIssue.userId.eq(userId))
            .fetchFirst()
    }
}