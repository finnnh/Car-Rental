package com.finn.carrental.api.dtos.pricing

import io.swagger.v3.oas.annotations.media.Schema

class BillingRequest(

    @Schema(example = "64c8c410bebeef1000d78c80")
    val userId: String,

    @Schema(example = "5")
    val month: Int,

    @Schema(example = "2023")
    val year: Int)