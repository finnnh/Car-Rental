package com.finn.carrental.api

import com.finn.carrental.api.dtos.CarResponse
import com.finn.carrental.api.dtos.CarResponse.Companion.toDTO
import com.finn.carrental.domain.CarService
import com.finn.carrental.domain.models.Car
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.function.EntityResponse

@RestController
class CarController(val carService: CarService) {

    @GetMapping("/cars")
    fun getAllCars(): ResponseEntity<List<CarResponse>> {
        return ResponseEntity.ok(carService.getAllCars().map { car -> car.toDTO() })
    }

}