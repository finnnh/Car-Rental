package com.finn.carrental.domain.models

import com.finn.carrental.persistence.entities.CarEntity

class Car(val id: String, val brand: String, val model: String, val seats: Int, val pricePerDistance: Double, val pricePerHour: Double) {

    companion object {
        fun CarEntity.toDomain(): Car {
            return Car(this.id.toString(), this.brand, this.model, this.seats, this.pricePerDistance, this.pricePerHour)
        }
    }
}
