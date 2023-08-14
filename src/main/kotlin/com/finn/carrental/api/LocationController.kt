package com.finn.carrental.api

import com.finn.carrental.api.dtos.locations.LocationRequest
import com.finn.carrental.api.dtos.locations.LocationResponse
import com.finn.carrental.api.dtos.locations.LocationResponse.Companion.toDTO
import com.finn.carrental.domain.LocationService
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
@RequestMapping("/locations")
@Tag(name = "Locations")
class LocationController(val locationService: LocationService) {

    @GetMapping()
    fun getAllLocations(): ResponseEntity<List<LocationResponse>> {
        return ResponseEntity(locationService.getAllLocations().map { location -> location.toDTO() }, HttpStatus.OK)
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
            ApiResponse(responseCode = "404", description = "No Location Found")
        ]
    )
    fun getLocationById(@PathVariable("id") id: String): ResponseEntity<LocationResponse> {
        val location = locationService.getLocationById(id)?.toDTO() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found")

        return ResponseEntity(location, HttpStatus.OK)
    }

    @GetMapping("/city/{city}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
            ApiResponse(responseCode = "404", description = "No Location Found")
        ]
    )
    fun getLocationByCity(@PathVariable("city") city: String): ResponseEntity<List<LocationResponse>> {
        val location = locationService.getLocationsByCity(city).map { it.toDTO() }
        if (location.isEmpty()) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found")

        return ResponseEntity(location, HttpStatus.OK)
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful")
        ]
    )
    fun createLocation(@RequestBody request: LocationRequest): ResponseEntity<LocationResponse> {
        return ResponseEntity(
            locationService.createLocation(
                request.houseNumber,
                request.street,
                request.postalCode,
                request.city
            ).toDTO(),
            HttpStatus.CREATED
        )
    }
}
