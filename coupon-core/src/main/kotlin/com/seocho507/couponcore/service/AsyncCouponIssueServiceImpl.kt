package com.seocho507.couponcore.service

import com.seocho507.couponcore.repository.redis.RedisRepository
import org.springframework.stereotype.Service

@Service
class AsyncCouponIssueService(
    private val redisRepository: RedisRepository,
    private val couponCacheService: CouponCacheService
) {
    fun issueCoupon(couponId: Long, userId: Long) {
        val couponLocalCache = couponCacheService.getCouponLocalCache(couponId)
        couponLocalCache.checkIssuableCoupon()
        issueRequest(couponId, userId, couponLocalCache.totalQuantity)
    }

    fun issueRequest(couponId: Long, userCouponId: Long, totalIssueQuantity: Int?) {
        val quantity = totalIssueQuantity ?: Int.MAX_VALUE
        redisRepository.issueRequest(couponId, userCouponId, quantity)
    }
}