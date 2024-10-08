package com.seocho507.couponapi

import com.seocho507.couponcore.CouponCoreConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(CouponCoreConfig::class)
@SpringBootApplication
class CouponApiApplication

fun main(args: Array<String>) {
    runApplication<CouponApiApplication>(*args) {
        setDefaultProperties(mapOf("spring.config.name" to "application-core, application-api"))
    }
}
