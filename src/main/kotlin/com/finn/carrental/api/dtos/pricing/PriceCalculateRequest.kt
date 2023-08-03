package com.finn.carrental.api.dtos.pricing

import io.swagger.v3.oas.annotations.media.Schema

class PriceCalculateRequest(

    @Schema(example = "64c8c410bebeef1000d78c80")
    val carId: String,

    @Schema(example = "5")
    val hours: Int,

    @Schema(example = "300")
    val km: Int
)
