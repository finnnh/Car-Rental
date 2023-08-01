package com.finn.carrental.domain

import com.finn.carrental.api.dtos.CarRequest
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

    fun createCar(carRequest: CarRequest): Car {
        return carRepository.save(
            CarEntity(
                brand = carRequest.brand,
                model = carRequest.model,
                seats = carRequest.seats
            )
        ).toDomain()
    }
}
