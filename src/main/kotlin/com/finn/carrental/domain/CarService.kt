package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.Car
import com.finn.carrental.domain.models.Car.Companion.toDomain
import com.finn.carrental.persistence.CarRepository
import com.finn.carrental.persistence.entities.CarEntity
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CarService(private val carRepository: CarRepository) {

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
    fun createCar(brand: String, model: String, seats: Int, pricePerDistance:Double, pricePerHour:Double): Car {
        return carRepository.save(
            CarEntity(
                brand = brand,
                model = model,
                seats = seats,
                pricePerDistance = pricePerDistance,
                pricePerHour = pricePerHour
            )
        ).toDomain()
    }
}
