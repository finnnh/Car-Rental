package com.finn.carrental.domain.models

import com.finn.carrental.persistence.entities.LocationEntity

class Location(val id: String, val houseNumber: Int, val street: String, val postalCode: Int, val city: String) {

    companion object {
        fun LocationEntity.toDomain(): Location {
            return Location(this.id.toString(), this.houseNumber, this.street, this.postalCode, this.city)
        }
    }

}
