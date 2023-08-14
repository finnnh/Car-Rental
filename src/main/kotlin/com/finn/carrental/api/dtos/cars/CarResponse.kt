package com.finn.carrental.api.dtos.cars

import com.finn.carrental.api.dtos.locations.LocationResponse
import com.finn.carrental.api.dtos.locations.LocationResponse.Companion.toDTO
import com.finn.carrental.domain.models.Car
import io.swagger.v3.oas.annotations.media.Schema

class CarResponse(

    @Schema(example = "64c8c410bebeef1000d78c80")
    val id: String,

    val location: LocationResponse,

    @Schema(example = "Audi")
    val brand: String,

    @Schema(example = "A4")
    val model: String,

    @Schema(example = "5")
    val seats: Int,

    @Schema(example = "7.5")
    val pricePerDistanceHigh: Double,

    @Schema(example = "5.5")
    val pricePerDistanceModerate: Double,

    @Schema(example = "2.5")
    val pricePerDistanceLow: Double,

    @Schema(example = "5.5")
    val pricePerHourHigh: Double,

    @Schema(example = "3.5")
    val pricePerHourModerate: Double,

    @Schema(example = "1.5")
    val pricePerHourLow: Double
) {

    companion object {
        fun Car.toDTO(): CarResponse {
            return CarResponse(
                this.id, this.location.toDTO(), this.brand, this.model, this.seats, this.pricePerDistanceHigh, this.pricePerDistanceModerate, this.pricePerDistanceLow,
                this.pricePerHourHigh, this.pricePerHourModerate, this.pricePerHourLow
            )
        }
    }
}
