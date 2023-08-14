package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.Car
import com.finn.carrental.domain.models.Car.Companion.toDomain
import com.finn.carrental.persistence.CarRepository
import com.finn.carrental.persistence.LocationRepository
import com.finn.carrental.persistence.entities.CarEntity
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CarService(private val carRepository: CarRepository, private val locationRepository: LocationRepository) {

    fun getAllCars(): List<Car> {
        return carRepository.findAll().map { carEntity -> carEntity.toDomain() }
    }

    fun getCarByID(id: String): Car? {
        return carRepository.findOneById(ObjectId(id))?.toDomain() ?: throw NotFoundException()
    }

    /**
     * Create a new Car
     * @param brand The Brand of the car
     * @param model The model of the car
     * @param seats The seats of the car
     * @return The newly created Car
     */
    fun createCar(locationId: String, brand: String, model: String, seats: Int, pricePerDistanceHigh: Double, pricePerDistanceModerate: Double, pricePerDistanceLow: Double, pricePerHourHigh: Double, pricePerHourModerate: Double, pricePerHourLow: Double): Car {
        val locationEntity = locationRepository.findOneById(ObjectId(locationId)) ?: throw NotFoundException("Location Not Found")

        return carRepository.save(
            CarEntity(
                locationEntity = locationEntity,
                brand = brand,
                model = model,
                seats = seats,
                pricePerDistanceHigh = pricePerDistanceHigh,
                pricePerDistanceModerate = pricePerDistanceModerate,
                pricePerDistanceLow = pricePerDistanceLow,
                pricePerHourHigh = pricePerHourHigh,
                pricePerHourModerate = pricePerHourModerate,
                pricePerHourLow = pricePerHourLow
            )
        ).toDomain()
    }
}
