package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.AlreadyRentedException
import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.Rental
import com.finn.carrental.domain.models.Rental.Companion.toDomain
import com.finn.carrental.persistence.CarRepository
import com.finn.carrental.persistence.RentalRepository
import com.finn.carrental.persistence.UserRepository
import com.finn.carrental.persistence.entities.RentalEntity
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RentalService(private val rentalRepository: RentalRepository, private val userRepository: UserRepository, private val carRepository: CarRepository) {

    fun getAllRentals(): List<Rental> {
        return rentalRepository.findAll().map { rentalEntity -> rentalEntity.toDomain() }
    }

    fun getRentalById(id: String): Rental? {
        return rentalRepository.findOneById(ObjectId(id))?.toDomain() ?: throw NotFoundException()
    }

    /**
     * @param userId The ObjectId of the User that wants to rent a car
     * @param carId The ObjectId of the Car that gets rented
     * @param start The Start Time of the Rent
     * @param end The End Time of the Rent
     * @throws NotFoundException If User or Car can not be found
     * @throws AlreadyRentedException If The Car is already Booked during the specific time period
     * @return The newly created Rent
     */
    fun createRental(userId: String, carId: String, start: LocalDateTime, end: LocalDateTime): Rental {
        val carEntity = carRepository.findOneById(ObjectId(carId)) ?: throw NotFoundException()

        val rentOfCarList = rentalRepository.findByCarEntity(carEntity)
        if (rentOfCarList.isNotEmpty()) {
            rentOfCarList.forEach {
                if (it.start.isEqual(start)) throw AlreadyRentedException()
                if (it.start.isBefore(start) && start.isBefore(it.end)) {
                    throw AlreadyRentedException()
                } else if (it.end.isAfter(end) && start.isBefore(it.start)) {
                    throw AlreadyRentedException()
                }
            }
        }

        val userEntity = userRepository.findOneById(ObjectId(userId)) ?: throw NotFoundException()
        return rentalRepository.save(
            RentalEntity(
                userEntity = userEntity,
                carEntity = carEntity,
                start = start,
                end = end
            )
        ).toDomain()
    }
}
