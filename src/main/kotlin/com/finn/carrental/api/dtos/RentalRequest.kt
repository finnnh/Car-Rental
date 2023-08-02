package com.finn.carrental.api.dtos

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
    val end: LocalDateTime
)
