package com.finn.carrental.api

import com.finn.carrental.api.dtos.pricing.BillingRequest
import com.finn.carrental.domain.BillingGeneratorService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream

@RestController
@RequestMapping("/billing")
@Tag(name = "Billing Generator")
class BillingGeneratorController(val billingGeneratorService: BillingGeneratorService) {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun generateBillingPDF(@RequestBody request: BillingRequest, response: HttpServletResponse): ResponseEntity<InputStreamResource> {
        val pdfBytes = billingGeneratorService.generateBillingPDF(request.userId, request.month, request.year)

        val headers = org.springframework.http.HttpHeaders()
        headers.add("Content-Disposition", "inline; filename=my-pdf-file.pdf")

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(pdfBytes.size.toLong())
            .body(InputStreamResource(ByteArrayInputStream(pdfBytes)))
    }
}
