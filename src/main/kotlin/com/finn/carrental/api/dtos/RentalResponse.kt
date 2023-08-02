package com.finn.carrental.api.dtos

import com.finn.carrental.api.dtos.CarResponse.Companion.toDTO
import com.finn.carrental.api.dtos.UserResponse.Companion.toDTO
import com.finn.carrental.domain.models.Rental

class RentalResponse(val id: String, val user: UserResponse, val car: CarResponse, val start: String, val end: String) {

    companion object {
        fun Rental.toDTO(): RentalResponse {
            return RentalResponse(this.id, this.user.toDTO(), this.car.toDTO(), this.start.toString(), this.end.toString())
        }
    }
}
