package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.persistence.CarRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.hours

@Service
class PriceCaluclatorService(private val carRepository: CarRepository) {

    fun calculatePriceForCar(carId: String, hours: Int, km: Int) {
        val car = carRepository.findOneById(ObjectId(carId)) ?: throw NotFoundException()

        val pricerPerHour = car.pricePerHour
        var pricePerDistance = car.pricePerDistance
        if(hours in 8..47) {
            pricePerDistance *= .75
        }

        if(hours >= 48) {
            pricePerDistance *= .5
        }

    }

}