package com.finn.carrental.api

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.finn.carrental.api.dtos.pricing.BillingRequest
import com.finn.carrental.domain.BillingGeneratorService
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.io.ByteArrayOutputStream

@WebMvcTest(BillingGeneratorController::class)
class BillingGeneratorControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var billingGeneratorService: BillingGeneratorService

    private val mapper = jacksonObjectMapper()

    @Test
    fun `getPDF() Should return a PDF`() {
        val document = Document()
        val byteArrayOutputStream = ByteArrayOutputStream()
        PdfWriter.getInstance(document, byteArrayOutputStream)
        document.open()
        val content = Chunk("Test")
        document.add(content)
        document.close()

        every { billingGeneratorService.generateBillingPDF(any(), any(), any()) } returns byteArrayOutputStream.toByteArray()

        val billing = BillingRequest("64c8c410bebeef1000d78c80", 8, 2023)

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val writer = mapper.writer().withDefaultPrettyPrinter()
        val requestJson = writer.writeValueAsString(billing)

        val result: MockHttpServletResponse = mockMvc.perform(
            MockMvcRequestBuilders.get("/billing").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_PDF)
        )
            .andExpect(status().isOk)
            .andReturn()
            .response

        val pdfBytes = billingGeneratorService.generateBillingPDF("64c8c410bebeef1000d78c80", 8, 2023)

        assert(result.contentType == MediaType.APPLICATION_PDF_VALUE)
        assert(result.contentLength == pdfBytes.size)
    }
}
