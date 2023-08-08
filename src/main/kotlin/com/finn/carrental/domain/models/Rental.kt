package com.finn.carrental.domain.models

import com.finn.carrental.domain.models.Car.Companion.toDomain
import com.finn.carrental.domain.models.User.Companion.toDomain
import com.finn.carrental.persistence.entities.RentalEntity
import java.time.LocalDateTime

class Rental(val id: String, val user: User, val car: Car, val start: LocalDateTime, val end: LocalDateTime, val hours: Int, val km: Int) {

    companion object {
        fun RentalEntity.toDomain(): Rental {
            return Rental(this.id.toString(), this.userEntity.toDomain(), this.carEntity.toDomain(), this.start, this.end, this.hours, this.km)
        }
    }
}
