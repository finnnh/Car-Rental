package com.finn.carrental.api

import com.finn.carrental.api.dtos.CarRequest
import com.finn.carrental.api.dtos.CarResponse
import com.finn.carrental.api.dtos.CarResponse.Companion.toDTO
import com.finn.carrental.domain.CarService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/cars")
class CarController(val carService: CarService) {

    @GetMapping("")
    fun getAllCars(): ResponseEntity<List<CarResponse>> {
        return ResponseEntity.ok(carService.getAllCars().map { car -> car.toDTO() })
    }

    @GetMapping("/{id}")
    fun getCarByID(@PathVariable("id") id: String): ResponseEntity<CarResponse> {
        val car = carService.getCarByID(id)?.toDTO() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found")

        return ResponseEntity(car, HttpStatus.OK)
    }

    @PostMapping("")
    fun createCar(@RequestBody request: CarRequest): ResponseEntity<CarResponse> {
        return ResponseEntity(carService.createCar(request).toDTO(), HttpStatus.CREATED)
    }

}