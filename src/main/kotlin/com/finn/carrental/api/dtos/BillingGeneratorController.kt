package com.finn.carrental.api.dtos

import com.finn.carrental.api.dtos.pricing.BillingRequest
import com.finn.carrental.domain.BillingGeneratorService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/billing")
@Tag(name = "Billing Generator")
class BillingGeneratorController(val billingGeneratorService: BillingGeneratorService) {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun generateBilling(@RequestBody request: BillingRequest, response: HttpServletResponse) {
        response.contentType = "application/pdf"

        val headerKey = "Content-Disposition"
        val headerValue = "attachment; filename=test.pdf"

        response.setHeader(headerKey, headerValue)

        billingGeneratorService.generateBillingPDF(response, request.userId, request.month, request.year)
    }
}