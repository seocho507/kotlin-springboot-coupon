package com.seocho507.couponapi.controller

import com.seocho507.couponapi.controller.request.CouponIssueRequest
import com.seocho507.couponapi.service.CouponIssueRequestService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/coupons")
@RestController
class CouponIssueController(
    private val couponIssueRequestService: CouponIssueRequestService
) {
    @PostMapping("/issue")
    fun issueCoupon(@RequestBody request: CouponIssueRequest) {
        couponIssueRequestService.issueRequest(request.couponId, request.userId)
    }
}