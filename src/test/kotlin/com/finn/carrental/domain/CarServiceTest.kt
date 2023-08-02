package com.finn.carrental.domain

import com.finn.carrental.api.dtos.CarRequest
import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.Car
import com.finn.carrental.persistence.CarRepository
import com.finn.carrental.persistence.entities.CarEntity
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CarServiceTest {

    private val carRepository: CarRepository = mockk()

    @Test
    fun `createCar() Should add a Car`() {
        // given
        val carService = CarService(carRepository)
        every { carRepository.save(any()) } returnsArgument(0)

        // when
        val car = carService.createCar("Audi", "A4", 5)

        // then
        val expectedCar = Car("", "Audi", "A4", 5)
        Assertions.assertThat(car).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedCar)
    }

    @Test
    fun `getAllCars() Should return multiple Cars`() {
        val carService = CarService(carRepository)
        every { carRepository.findAll() } returns listOf(CarEntity(model = "A4", brand = "Audi", seats = 5), CarEntity(model = "M4", brand = "BMW", seats = 5))

        // when
        val list = carService.getAllCars()

        // then
        Assertions.assertThat(list).isNotEmpty()
    }

    @Test
    fun `getCarByID() Should return a Car with the given ID`() {
        // given
        val carService = CarService(carRepository)
        every { carRepository.findOneById(any()) } returns CarEntity(ObjectId("64c8fb032eb57e0b2626907c"), "Audi", "A4", 5)

        // when
        val car = carService.getCarByID("64c8fb032eb57e0b2626907c")

        Assertions.assertThat(car!!.id).isEqualTo("64c8fb032eb57e0b2626907c")
    }

    @Test
    fun `getCarByIDThatDoenstExists() Should throw NotFoundException`() {
        // given
        val carService = CarService(carRepository)
        every { carRepository.findOneById(any()) } returns null

        // then
        assertThrows<NotFoundException> {
            // when
            carService.getCarByID("64c7a03aa6148808920a8ab1")
        }
    }
}
