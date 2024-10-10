package com.seocho507.couponcore.repository.redis

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.seocho507.couponcore.exception.CouponIssueException
import com.seocho507.couponcore.util.CouponRedisUtils.getIssueRequestKey
import com.seocho507.couponcore.util.CouponRedisUtils.getIssueRequestQueueKey
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Repository

@Repository
class RedisRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val issueScript: RedisScript<String> = issueRequestScript()
    private val issueRequestQueueKey: String = getIssueRequestQueueKey()
    private val objectMapper: ObjectMapper = ObjectMapper()


    fun zAdd(key: String, value: String, score: Double): Boolean? {
        return redisTemplate.opsForZSet().addIfAbsent(key, value, score)
    }

    fun sAdd(key: String, value: String): Long? {
        return redisTemplate.opsForSet().add(key, value)
    }

    fun sCard(key: String): Long? {
        return redisTemplate.opsForSet().size(key)
    }

    fun sIsMember(key: String, value: String): Boolean? {
        return redisTemplate.opsForSet().isMember(key, value)
    }

    fun rPush(key: String, value: String): Long? {
        return redisTemplate.opsForList().rightPush(key, value)
    }

    fun lIndex(key: String, index: Long): String? {
        return redisTemplate.opsForList().index(key, index)
    }

    fun lPop(key: String): String? {
        return redisTemplate.opsForList().leftPop(key)
    }

    fun lSize(key: String): Long? {
        return redisTemplate.opsForList().size(key)
    }

    fun issueRequest(couponId: Long, userId: Long, totalIssueQuantity: Int) {
        val issueRequestKey = getIssueRequestKey(couponId)
        val couponIssueRequest = CouponIssueRequest(couponId, userId)
        try {
            val code: String = redisTemplate.execute(
                issueScript,
                listOf(issueRequestKey, issueRequestQueueKey),
                userId.toString(),
                totalIssueQuantity.toString(),
                objectMapper.writeValueAsString(couponIssueRequest)
            )
            CouponIssueRequestCode.checkRequestResult(CouponIssueRequestCode.find(code))
        } catch (e: JsonProcessingException) {
            throw CouponIssueException.FailCouponIssueException()
        }
    }

    private fun issueRequestScript(): RedisScript<String> {
        val script = """
            if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
                return '2'
            end

            if tonumber(ARGV[2]) > redis.call('SCARD', KEYS[1]) then
                redis.call('SADD', KEYS[1], ARGV[1])
                redis.call('RPUSH', KEYS[2], ARGV[3])
                return '1'
            end

            return '3'
        """.trimIndent()
        return RedisScript.of(script, String::class.java)
    }
}