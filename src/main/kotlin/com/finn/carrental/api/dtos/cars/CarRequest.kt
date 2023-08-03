package com.finn.carrental.api.dtos.cars

import io.swagger.v3.oas.annotations.media.Schema

class CarRequest(

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

)
