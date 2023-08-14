package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.Location
import com.finn.carrental.domain.models.Location.Companion.toDomain
import com.finn.carrental.persistence.LocationRepository
import com.finn.carrental.persistence.entities.LocationEntity
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class LocationService(private val locationRepository: LocationRepository) {

    fun createLocation(houseNumber: Int, street: String, postalCode: Int, city: String): Location {
        return locationRepository.save(
            LocationEntity(
                houseNumber = houseNumber,
                street = street,
                postalCode = postalCode,
                city = city
            )
        ).toDomain()
    }

    fun getAllLocations(): List<Location> {
        return locationRepository.findAll().map { locationEntity -> locationEntity.toDomain() }
    }

    fun getLocationById(id: String): Location? {
        return locationRepository.findOneById(ObjectId(id))?.toDomain() ?: throw NotFoundException()
    }

    fun getLocationsByCity(city: String): List<Location> {
        return locationRepository.findByCity(city).map { it.toDomain() }
    }
}
