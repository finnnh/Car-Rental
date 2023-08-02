package com.finn.carrental.api

import com.finn.carrental.api.dtos.RentalRequest
import com.finn.carrental.api.dtos.RentalResponse
import com.finn.carrental.api.dtos.RentalResponse.Companion.toDTO
import com.finn.carrental.domain.RentalService
import com.finn.carrental.domain.exceptions.AlreadyRentedException
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
@RequestMapping("/rentals")
class RentalController(val rentalService: RentalService) {

    @GetMapping("")
    fun getAllRentals(): ResponseEntity<List<RentalResponse>> {
        return ResponseEntity.ok(rentalService.getAllRentals().map { rental -> rental.toDTO() })
    }

    @GetMapping("{id}")
    fun getRentalByID(@PathVariable("id") id: String): ResponseEntity<RentalResponse> {
        val rental = rentalService.getRentalById(id)?.toDTO() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found")

        return ResponseEntity(rental, HttpStatus.OK)
    }

    @PostMapping("")
    fun createRental(@RequestBody request: RentalRequest): ResponseEntity<RentalResponse> {
        try {
            return ResponseEntity(rentalService.createRental(request.userId, request.carId, request.start, request.end).toDTO(), HttpStatus.CREATED)
        } catch (exception: AlreadyRentedException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Car Already Rented")
        }
    }
}
