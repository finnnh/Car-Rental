package com.finn.carrental.api

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.finn.carrental.api.dtos.RentalRequest
import com.finn.carrental.domain.RentalService
import com.finn.carrental.domain.exceptions.AlreadyRentedException
import com.finn.carrental.domain.models.Car
import com.finn.carrental.domain.models.Rental
import com.finn.carrental.domain.models.User
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
import java.time.LocalDateTime

@WebMvcTest(RentalController::class)
class RentalControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var rentalService: RentalService

    private val mapper = jacksonObjectMapper().apply { this.registerModule(JavaTimeModule()) }

    @Test
    fun `getRentalByID() Should Return a Rental with ID 64c8c410bebeef1000d78c80`() {
        every { rentalService.getRentalById(any()) } returns
            Rental(
                "64c8c410bebeef1000d78c80",
                User("Finn", "64c8c410bebeef1000d78c80", "Hoffmann", "fihoffmann@web.de"),
                Car("64c8fb032eb57e0b2626907c", "Audi", "A5", 5),
                LocalDateTime.of(2023, 8, 20, 12, 0),
                LocalDateTime.of(2023, 8, 25, 12, 0)
            )

        mockMvc.perform(MockMvcRequestBuilders.get("/rentals/64c8c410bebeef1000d78c80"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("id").value("64c8c410bebeef1000d78c80"))
    }

    @Test
    fun `getRentalByIDThatDoenstExist() Should throw a Response Status Exception (NOT FOUND)`() {
        every { rentalService.getRentalById(any()) } returns null

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/64c8c410bebeef1000d78c80"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `getAllRentals() Should return a list of Rentals`() {
        every { rentalService.getAllRentals() } returns listOf(
            Rental(
                "64c8c410bebeef1000d78c80",
                User("Finn", "64c8c410bebeef1000d78c80", "Hoffmann", "fihoffmann@web.de"),
                Car("64c8fb032eb57e0b2626907c", "Audi", "A5", 5),
                LocalDateTime.of(2023, 8, 20, 12, 0),
                LocalDateTime.of(2023, 8, 25, 12, 0)
            ),

            Rental(
                "64c8c410bebeef1000d78c80",
                User("Kostas", "64c8c410bebeef1000d78c80", "Lastname", "test@gmail.com"),
                Car("64c8fb032eb57e0b2626907c", "BMW", "M5", 5),
                LocalDateTime.of(2023, 8, 20, 12, 0),
                LocalDateTime.of(2023, 8, 25, 12, 0)
            )
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/rentals"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val rentals: List<Rental> = mapper.readValue(result.response.contentAsString)

        Assertions.assertThat(rentals).isNotEmpty
    }

    @Test
    fun `createRental() Should return the created Rental & Created Status`() {
        every { rentalService.createRental(any(), any(), any(), any()) } returns
            Rental(
                "64c8c410bebeef1000d78c80",
                User("Finn", "64c8c410bebeef1000d78c80", "Hoffmann", "fihoffmann@web.de"),
                Car("64c8fb032eb57e0b2626907c", "Audi", "A5", 5),
                LocalDateTime.of(2023, 8, 20, 12, 0),
                LocalDateTime.of(2023, 8, 25, 12, 0)
            )

        val rental = RentalRequest(
            "64c8c410bebeef1000d78c80",
            "64c8fb032eb57e0b2626907c",
            LocalDateTime.of(2023, 8, 20, 12, 0),
            LocalDateTime.of(2023, 8, 25, 12, 0)
        )

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val writer = mapper.writer().withDefaultPrettyPrinter()

        val requestJson = writer.writeValueAsString(rental)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/rentals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
            MockMvcResultMatchers.status().isCreated
        )
    }

    @Test
    fun `createRentalInABookedTimeframe() Should HttpStatus Conflict`() {
        every { rentalService.createRental(any(), any(), any(), any()) } throws AlreadyRentedException()

        val rental = RentalRequest(
            "64c8c410bebeef1000d78c80",
            "64c8fb032eb57e0b2626907c",
            LocalDateTime.of(2023, 8, 20, 12, 0),
            LocalDateTime.of(2023, 8, 25, 12, 0)
        )

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val writer = mapper.writer().withDefaultPrettyPrinter()

        val requestJson = writer.writeValueAsString(rental)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/rentals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
            MockMvcResultMatchers.status().isConflict
        )
    }
}
