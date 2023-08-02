package com.finn.carrental.api.dtos

import io.swagger.v3.oas.annotations.media.Schema

class CarRequest(

    @Schema(example = "Audi")
    val brand: String,

    @Schema(example = "A4")
    val model: String,

    @Schema(example = "5")
    val seats: Int
)
