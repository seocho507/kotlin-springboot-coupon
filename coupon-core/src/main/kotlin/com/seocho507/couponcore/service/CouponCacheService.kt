package com.seocho507.couponcore.service

import com.seocho507.couponcore.entity.Coupon
import com.seocho507.couponcore.repository.redis.CouponRedisEntity
import org.springframework.aop.framework.AopContext
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CouponCacheService(
    private val couponIssueService: CouponIssueService
) {

    @Cacheable(cacheNames = ["coupon"])
    fun getCouponCache(couponId: Long): CouponRedisEntity {
        val coupon: Coupon = couponIssueService.findCoupon(couponId)
        return CouponRedisEntity(coupon)
    }

    @CachePut(cacheNames = ["coupon"])
    fun putCouponCache(couponId: Long): CouponRedisEntity {
        return getCouponCache(couponId)
    }

    @Cacheable(cacheNames = ["coupon"], cacheManager = "localCacheManager")
    fun getCouponLocalCache(couponId: Long): CouponRedisEntity {
        return proxy().getCouponCache(couponId)
    }

    @CachePut(cacheNames = ["coupon"], cacheManager = "localCacheManager")
    fun putCouponLocalCache(couponId: Long): CouponRedisEntity {
        return getCouponLocalCache(couponId)
    }

    private fun proxy(): CouponCacheService {
        return AopContext.currentProxy() as CouponCacheService
    }
}