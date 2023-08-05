package com.finn.carrental.api

import com.finn.carrental.api.dtos.rentals.RentalRequest
import com.finn.carrental.api.dtos.rentals.RentalResponse
import com.finn.carrental.api.dtos.rentals.RentalResponse.Companion.toDTO
import com.finn.carrental.domain.RentalService
import com.finn.carrental.domain.exceptions.AlreadyRentedException
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
@RequestMapping("/rentals")
@Tag(name = "Rentals")
class RentalController(val rentalService: RentalService) {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful")
        ]
    )
    fun getAllRentals(): ResponseEntity<List<RentalResponse>> {
        return ResponseEntity.ok(rentalService.getAllRentals().map { rental -> rental.toDTO() })
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
            ApiResponse(responseCode = "404", description = "No Rental with this ID")
        ]
    )
    fun getRentalByID(@PathVariable("id") id: String): ResponseEntity<RentalResponse> {
        val rental = rentalService.getRentalById(id)?.toDTO() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found")

        return ResponseEntity(rental, HttpStatus.OK)
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
            ApiResponse(responseCode = "409", description = "There is already a Rent for this Car")
        ]
    )
    fun createRental(@RequestBody request: RentalRequest): ResponseEntity<RentalResponse> {
        try {
            return ResponseEntity(rentalService.createRental(request.userId, request.carId, request.start, request.end, request.hours, request.km).toDTO(), HttpStatus.CREATED)
        } catch (exception: AlreadyRentedException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Car Already Rented")
        }
    }
}
