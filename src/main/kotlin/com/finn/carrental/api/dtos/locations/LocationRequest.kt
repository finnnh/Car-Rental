package com.finn.carrental.api.dtos.locations

import io.swagger.v3.oas.annotations.media.Schema

class LocationRequest(

    @Schema(example = "5")
    val houseNumber: Int,

    @Schema(example = "Stiftsgasse")
    val street: String,

    @Schema(example = "53111")
    val postalCode: Int,

    @Schema(example = "Bonn")
    val city: String

)
