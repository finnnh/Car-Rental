package com.finn.carrental.api.dtos.locations

import com.finn.carrental.domain.models.Location
import io.swagger.v3.oas.annotations.media.Schema

class LocationResponse(

    @Schema(example = "64c8c410bebeef1000d78c80")
    val id: String,

    @Schema(example = "5")
    val houseNumber: Int,

    @Schema(example = "Stiftsgasse")
    val street: String,

    @Schema(example = "53111")
    val postalCode: Int,

    @Schema(example = "Bonn")
    val city: String

) {
    companion object {
        fun Location.toDTO(): LocationResponse {
            return LocationResponse(this.id, this.houseNumber, this.street, this.postalCode, this.city)
        }
    }
}
