package com.finn.carrental.api

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.finn.carrental.api.dtos.cars.CarRequest
import com.finn.carrental.domain.CarService
import com.finn.carrental.domain.models.Car
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CarController::class)
class CarControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var carService: CarService

    private val mapper = jacksonObjectMapper()

    @Test
    fun `getCarByID() Should Return a Car with ID 64c8c410bebeef1000d78c80`() {
        every { carService.getCarByID(any()) } returns Car("64c8c410bebeef1000d78c80", "Audi", "A4", 5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0)

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/64c8c410bebeef1000d78c80"))
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id").value("64c8c410bebeef1000d78c80"))
    }

    @Test
    fun `getCarByIDThatDoenstExist() Should throw a Response Status Exception (NOT FOUND)`() {
        every { carService.getCarByID(any()) } returns null

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/64c8c410bebeef1000d78c80"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `getAllCars() Should return a list of Cars`() {
        every { carService.getAllCars() } returns listOf(Car("64c8c410bebeef1000d78c80", "Audi", "A4", 5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0), Car("64c8c410bebeef1000d78c81", "BMW", "M4", 4, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0))

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/cars"))
            .andExpect(status().isOk)
            .andReturn()

        val cars: List<Car> = mapper.readValue(result.response.contentAsString)

        Assertions.assertThat(cars).isNotEmpty
    }

    @Test
    fun `createCar() Should return the created Car & Created Status`() {
        every { carService.createCar(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Car("64c8c410bebeef1000d78c80", "Audi", "A4", 5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0)

        val car = CarRequest("Audi", "A4", 5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0)

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val writer = mapper.writer().withDefaultPrettyPrinter()

        val requestJson = writer.writeValueAsString(car)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
            status().isCreated
        )
    }
}
