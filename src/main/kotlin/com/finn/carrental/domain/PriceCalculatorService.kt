package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.persistence.CarRepository
import com.finn.carrental.persistence.entities.CarEntity
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PriceCalculatorService(private val carRepository: CarRepository, @Value("\${pricing-calculator-type}") val pricingType: String) {

    /**
     * @param carId The ObjectId as String of the car
     * @param hours The amount of hours that the car is rented
     * @param km The amount of km that the car will drive while it is rented
     * @return The price of the Car as a Double
     */
    fun calculatePrice(carId: String, hours: Int, km: Int): Double {
        return when (pricingType) {
            "steps" -> calculatePriceForCarSteps(carId, hours, km)
            "linear" -> calculatePriceForCarLinear(carId, hours, km)
            else -> {
                calculatePriceForCarSteps(carId, hours, km)
            }
        }
    }

    fun calculatePriceForCarLinear(carId: String, hours: Int, km: Int): Double {
        val car = carRepository.findOneById(ObjectId(carId)) ?: throw NotFoundException()

        var finalPrice = 0.0

        finalPrice += calculatePriceLinearHour(car, hours)
        finalPrice += calculatePriceLinearKM(car, km)

        return finalPrice
    }

    fun linearFunction(x: Double, slope: Double, yIntercept: Double): Double {
        return slope * x + yIntercept
    }

    fun calculatePriceForCarSteps(carId: String, hours: Int, km: Int): Double {
        val car = carRepository.findOneById(ObjectId(carId)) ?: throw NotFoundException()

        var finalPrice = 0.0
        finalPrice += calculatePriceStepsHour(car, hours)
        finalPrice += calculatePriceStepsKM(car, km)

        return finalPrice
    }

    fun calculatePriceStepsHour(car: CarEntity, hours: Int): Double {
        var finalPrice = 0.0

        var hoursLeft = hours

        if (hours in 0..8) {
            finalPrice += car.pricePerHourHigh * hoursLeft
            hoursLeft = 0
        } else if (hoursLeft > 8) {
            finalPrice += car.pricePerHourHigh * 8
            hoursLeft -= 8
        }

        if (hours in 0..8) {
            finalPrice += car.pricePerHourModerate * hoursLeft
            hoursLeft = 0
        } else if (hoursLeft > 48) {
            finalPrice += car.pricePerHourModerate * 48
            hoursLeft -= 48
        }

        if (hours > 0) finalPrice += car.pricePerHourLow * hoursLeft

        return finalPrice
    }

    fun calculatePriceStepsKM(car: CarEntity, km: Int): Double {
        var finalPrice = 0.0
        var kmLeft = km

        if (kmLeft in 0..30) {
            finalPrice += car.pricePerDistanceHigh * kmLeft
            kmLeft = 0
        } else if (kmLeft > 30) {
            finalPrice += car.pricePerDistanceHigh * 30
            kmLeft -= 30
        }

        if (kmLeft in 0..70) {
            finalPrice += car.pricePerDistanceModerate * kmLeft
            kmLeft = 0
        } else if (kmLeft > 70) {
            finalPrice += car.pricePerDistanceModerate * 70
            kmLeft -= 70
        }

        if (kmLeft > 0) finalPrice += car.pricePerDistanceLow * kmLeft

        return finalPrice
    }

    fun calculatePriceLinearKM(car: CarEntity, km: Int): Double {
        var finalPrice = 0.0

        for (i in 1..km) {
            val linear = linearFunction(i.toDouble(), -0.1, car.pricePerHourHigh)
            val value = if (linear > 1) linear else 1.0

            finalPrice += value
        }

        return finalPrice
    }

    fun calculatePriceLinearHour(car: CarEntity, hours: Int): Double {
        var finalPrice = 0.0

        for (i in 1..hours) {
            val linear = linearFunction(i.toDouble(), -0.1, car.pricePerDistanceHigh)
            val value = if (linear > 1) linear else 1.0

            finalPrice += value
        }

        return finalPrice
    }
}
