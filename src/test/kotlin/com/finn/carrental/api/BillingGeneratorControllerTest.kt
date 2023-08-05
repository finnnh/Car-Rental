package com.finn.carrental.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.finn.carrental.domain.BillingGeneratorService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(BillingGeneratorController::class)
class BillingGeneratorControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var billingGeneratorService: BillingGeneratorService

    private val mapper = jacksonObjectMapper()

    @Test
    fun `getPDF() Should return a PDF`() {
        every { billingGeneratorService.generateBillingPDF(any(), any(), any(), any()) }
    }

}