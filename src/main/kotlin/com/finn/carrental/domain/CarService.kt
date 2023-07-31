package com.finn.carrental.domain

import com.finn.carrental.domain.models.Car
import com.finn.carrental.domain.models.Car.Companion.toDomain
import com.finn.carrental.persistence.CarRepository

class CarService(private val carRepository: CarRepository) {

    fun getAllCars(): List<Car> {
        return carRepository.findAll().map { carEntity -> carEntity.toDomain() };
    }

}