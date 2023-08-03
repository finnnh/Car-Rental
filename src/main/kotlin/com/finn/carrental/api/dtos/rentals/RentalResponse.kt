package com.finn.carrental.api.dtos.rentals

import com.finn.carrental.api.dtos.cars.CarResponse
import com.finn.carrental.api.dtos.cars.CarResponse.Companion.toDTO
import com.finn.carrental.api.dtos.users.UserResponse
import com.finn.carrental.api.dtos.users.UserResponse.Companion.toDTO
import com.finn.carrental.domain.models.Rental
import io.swagger.v3.oas.annotations.media.Schema

class RentalResponse(

    @Schema(example = "64c8c410bebeef1000d78c80")
    val id: String,

    val user: UserResponse,
    val car: CarResponse,

    @Schema(example = "2023-08-04T06:28:53.000Z")
    val start: String,

    @Schema(example = "2023-08-07T06:28:53.000Z")
    val end: String
) {

    companion object {
        fun Rental.toDTO(): RentalResponse {
            return RentalResponse(this.id, this.user.toDTO(), this.car.toDTO(), this.start.toString(), this.end.toString())
        }
    }
}
