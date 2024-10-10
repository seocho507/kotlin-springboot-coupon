package com.seocho507.couponapi.controller

import com.seocho507.couponapi.controller.response.CouponIssueResponse
import com.seocho507.couponcore.exception.CouponException
import com.seocho507.couponcore.exception.CouponIssueException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.seocho507.couponapi.controller"])
class CouponIssueControllerAdvice {

    @ExceptionHandler(CouponIssueException::class)
    fun couponIssueExceptionHandler(exception: CouponIssueException): CouponIssueResponse {
        return CouponIssueResponse(false, exception.errorCode.message)
    }

    @ExceptionHandler(CouponException::class)
    fun couponExceptionHandler(exception: CouponException): CouponIssueResponse {
        return CouponIssueResponse(false, exception.errorCode.message)
    }
}