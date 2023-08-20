package com.finn.carrental.api.dtos.rentals

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

class RentalRequest(

    @Schema(example = "64c8c410bebeef1000d78c80")
    val userId: String,

    @Schema(example = "64c8c410bebeef1120d78c90")
    val carId: String,

    @Schema(example = "2023-08-04T06:28:53.000Z")
    val start: LocalDateTime,

    @Schema(example = "2023-08-08T06:28:53.000Z")
    val end: LocalDateTime,

    @Schema(example = "48")
    val hours: Int,

    @Schema(example = "300")
    val km: Int,

    @Schema(example = "64c8c410bebeef1000d78c80")
    val startLocationId: String,

    @Schema(example = "64c8c410bebeef1000d78c80")
    val endLocationId: String
)
