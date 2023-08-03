package com.finn.carrental.domain.models

import com.finn.carrental.persistence.entities.CarEntity

class Car(
    val id: String,
    val brand: String,
    val model: String,
    val seats: Int,

    var pricePerDistanceHigh: Double,
    var pricePerDistanceModerate: Double,
    var pricePerDistanceLow: Double,
    var pricePerHourHigh: Double,
    var pricePerHourModerate: Double,
    var pricePerHourLow: Double
) {

    companion object {
        fun CarEntity.toDomain(): Car {
            return Car(
                this.id.toString(), this.brand, this.model, this.seats,
                this.pricePerDistanceHigh,
                this.pricePerDistanceModerate,
                this.pricePerDistanceLow,
                this.pricePerHourHigh,
                this.pricePerHourModerate,
                this.pricePerHourLow
            )
        }
    }
}
