package com.finn.carrental.api.dtos

import com.finn.carrental.domain.models.Car

class CarResponse(val id: String, val brand: String, val model: String, val seats: Int) {

    companion object {
        fun Car.toDTO(): CarResponse {
            return CarResponse(this.id, this.brand, this.model, this.seats)
        }
    }

}