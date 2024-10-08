package com.seocho507.couponconsumer

import com.seocho507.couponcore.CouponCoreConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(CouponCoreConfig::class)
@SpringBootApplication
class CouponConsumerApplication

fun main(args: Array<String>) {
    runApplication<CouponConsumerApplication>(*args) {
        setDefaultProperties(mapOf("spring.config.name" to "application-core, application-consumer"))
    }
}
