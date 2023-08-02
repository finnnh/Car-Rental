package com.finn.carrental.api.dtos

import com.finn.carrental.domain.models.Car
import io.swagger.v3.oas.annotations.media.Schema

class CarResponse(

    @Schema(example = "64c8c410bebeef1000d78c80")
    val id: String,

    @Schema(example = "Audi")
    val brand: String,

    @Schema(example = "A4")
    val model: String,

    @Schema(example = "5")
    val seats: Int
) {

    companion object {
        fun Car.toDTO(): CarResponse {
            return CarResponse(this.id, this.brand, this.model, this.seats)
        }
    }
}
