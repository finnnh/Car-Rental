package com.finn.carrental.api

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.finn.carrental.api.dtos.locations.LocationRequest
import com.finn.carrental.domain.LocationService
import com.finn.carrental.domain.models.Location
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

@WebMvcTest(LocationController::class)
class LocationControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var locationService: LocationService

    private val mapper = jacksonObjectMapper().apply { this.registerModule(JavaTimeModule()) }

    @Test
    fun `getLocationByID() Should Return a Location with ID 64c8c410bebeef1000d78c80`() {
        every { locationService.getLocationById(any()) } returns
            Location(
                "64c8c410bebeef1000d78c80",
                80,
                "Breite Strasse",
                50667,
                "Cologne"
            )

        mockMvc.perform(MockMvcRequestBuilders.get("/locations/id/64c8c410bebeef1000d78c80"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("id").value("64c8c410bebeef1000d78c80"))
    }

    @Test
    fun `getLocationByCity() Should Return a Location List`() {
        every { locationService.getLocationsByCity(any()) } returns listOf(
            Location(
                "64c8c410bebeef1000d78c80",
                80,
                "Breite Strasse",
                50667,
                "Cologne"
            )
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/locations/city/Cologne"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val locations: List<Location> = mapper.readValue(result.response.contentAsString)

        Assertions.assertThat(locations).isNotEmpty
    }

    @Test
    fun `getLocationByIDThatDoenstExist() Should throw a Response Status Exception (NOT FOUND)`() {
        every { locationService.getLocationById(any()) } returns null

        mockMvc.perform(MockMvcRequestBuilders.get("/locations/id/64c8c410bebeef1000d78c80"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `getAllLocations() Should return a list of Rentals`() {
        every { locationService.getAllLocations() } returns listOf(
            Location(
                "64c8c410bebeef1000d78c80",
                80,
                "Breite Strasse",
                50667,
                "Cologne"
            ),
            Location(
                "64d4b15e0632c87bd89d3512",
                2,
                "Im Zollhafen",
                50678,
                "Cologne"
            )
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/locations"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val locations: List<Location> = mapper.readValue(result.response.contentAsString)

        Assertions.assertThat(locations).isNotEmpty
    }

    @Test
    fun `createLocation() Should return the created LOcation & Created Status`() {
        every { locationService.createLocation(any(), any(), any(), any()) } returns
            Location(
                "64d4b15e0632c87bd89d3512",
                2,
                "Im Zollhafen",
                50678,
                "Cologne"
            )

        val location = LocationRequest(
            2,
            "Im Zollhafen",
            50678,
            "Cologne"
        )

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val writer = mapper.writer().withDefaultPrettyPrinter()

        val requestJson = writer.writeValueAsString(location)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
            MockMvcResultMatchers.status().isCreated
        )
    }
}
