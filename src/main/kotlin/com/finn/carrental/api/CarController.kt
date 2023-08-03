package com.finn.carrental.api

import com.finn.carrental.api.dtos.cars.CarRequest
import com.finn.carrental.api.dtos.cars.CarResponse
import com.finn.carrental.api.dtos.cars.CarResponse.Companion.toDTO
import com.finn.carrental.domain.CarService
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/cars")
@Tag(name = "Cars")
class CarController(val carService: CarService) {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful")
        ]
    )
    fun getAllCars(): ResponseEntity<List<CarResponse>> {
        return ResponseEntity.ok(carService.getAllCars().map { car -> car.toDTO() })
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
            ApiResponse(responseCode = "404", description = "No Car with this ID")
        ]
    )
    fun getCarByID(@PathVariable("id") id: String): ResponseEntity<CarResponse> {
        val car = carService.getCarByID(id)?.toDTO() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found")

        return ResponseEntity(car, HttpStatus.OK)
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful")
        ]
    )
    fun createCar(@RequestBody request: CarRequest): ResponseEntity<CarResponse> {
        return ResponseEntity(
            carService.createCar(
                request.brand, request.model, request.seats,
                request.pricePerDistanceHigh,
                request.pricePerDistanceModerate,
                request.pricePerDistanceLow,
                request.pricePerHourHigh,
                request.pricePerHourModerate,
                request.pricePerHourLow
            ).toDTO(),
            HttpStatus.CREATED
        )
    }
}
