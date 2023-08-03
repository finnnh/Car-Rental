package com.finn.carrental.api

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.finn.carrental.api.dtos.pricing.PriceCalculateRequest
import com.finn.carrental.domain.PriceCalculatorService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(PriceCalculatorController::class)
class PriceCalculatorControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var priceCalculatorService: PriceCalculatorService

    private val mapper = jacksonObjectMapper()

    @Test
    fun `getPriceOfCar() Should return the price of the car`() {
        every { priceCalculatorService.calculatePrice(any(), any(), any()) } returns 5.0
        every { priceCalculatorService.calculatePriceForCarSteps(any(), any(), any()) } returns 5.0
        every { priceCalculatorService.calculatePriceForCarLinear(any(), any(), any()) } returns 5.0

        val price = PriceCalculateRequest("64ca5e14df5d3d4585ad2fa2", 5, 300)

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)

        val writer = mapper.writer().withDefaultPrettyPrinter()
        val requestJson = writer.writeValueAsString(price)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/pricecalc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(MockMvcResultMatchers.status().isOk).andReturn()

        val info: Double = mapper.readValue(result.response.contentAsString)

        Assertions.assertThat(info).isEqualTo(5.0)
    }
}
