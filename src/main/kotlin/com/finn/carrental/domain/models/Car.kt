package com.finn.carrental.domain.models

import com.finn.carrental.domain.models.Location.Companion.toDomain
import com.finn.carrental.persistence.entities.CarEntity

class Car(
    val id: String,

    val location: Location,

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
                this.id.toString(), this.locationEntity.toDomain(), this.brand, this.model, this.seats,
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
