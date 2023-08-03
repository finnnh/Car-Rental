package com.finn.carrental.api

import com.finn.carrental.api.dtos.pricing.PriceCalculateRequest
import com.finn.carrental.domain.PriceCalculatorService
import com.finn.carrental.domain.exceptions.NotFoundException
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/pricecalc")
@Tag(name = "Price Calculator")
class PriceCalculatorController(val priceCalculatorService: PriceCalculatorService) {

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    fun getPrice(@RequestBody request: PriceCalculateRequest): ResponseEntity<Double> {
        try {
            return ResponseEntity(priceCalculatorService.calculatePrice(request.carId, request.hours, request.km), HttpStatus.OK)
        } catch (exception: NotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }
}
