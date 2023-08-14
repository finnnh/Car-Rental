package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.Car
import com.finn.carrental.domain.models.Location
import com.finn.carrental.persistence.CarRepository
import com.finn.carrental.persistence.LocationRepository
import com.finn.carrental.persistence.entities.CarEntity
import com.finn.carrental.persistence.entities.LocationEntity
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CarServiceTest {

    private val carRepository: CarRepository = mockk()
    private val locationRepository: LocationRepository = mockk()

    @Test
    fun `createCar() Should add a Car`() {
        // given
        val carService = CarService(carRepository, locationRepository)
        every { carRepository.save(any()) } returnsArgument(0)
        every { locationRepository.findOneById(any()) } returns LocationEntity(
            ObjectId("64c8c410bebeef1000d78c80"),
            80,
            "Breite Strasse",
            50667,
            "Cologne"
        )

        // when
        val car = carService.createCar("64d4b15e0632c87bd89d3512", "Audi", "A4", 5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0)

        // then
        val expectedCar = Car(
            "",
            Location(
                "64c8c410bebeef1000d78c80",
                80,
                "Breite Strasse",
                50667,
                "Cologne"
            ),
            "Audi", "A4", 5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0
        )
        Assertions.assertThat(car).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedCar)
    }

    @Test
    fun `getAllCars() Should return multiple Cars`() {
        val carService = CarService(carRepository, locationRepository)
        every { carRepository.findAll() } returns listOf(
            CarEntity(
                locationEntity = LocationEntity(
                    ObjectId("64d4b15e0632c87bd89d3512"),
                    80,
                    "Breite Strasse",
                    50667,
                    "Cologne"
                ),
                model = "A4", brand = "Audi", seats = 5, pricePerDistanceHigh = 5.0, pricePerDistanceModerate = 5.0, pricePerDistanceLow = 5.0, pricePerHourHigh = 5.0, pricePerHourModerate = 5.0, pricePerHourLow = 5.0
            ),
            CarEntity(
                locationEntity = LocationEntity(
                    ObjectId("64d4b15e0632c87bd89d3512"),
                    80,
                    "Breite Strasse",
                    50667,
                    "Cologne"
                ),
                model = "M4", brand = "BMW", seats = 5, pricePerDistanceHigh = 5.0, pricePerDistanceModerate = 5.0, pricePerDistanceLow = 5.0, pricePerHourHigh = 5.0, pricePerHourModerate = 5.0, pricePerHourLow = 5.0
            )
        )

        // when
        val list = carService.getAllCars()

        // then
        Assertions.assertThat(list).isNotEmpty()
    }

    @Test
    fun `getCarByID() Should return a Car with the given ID`() {
        // given
        val carService = CarService(carRepository, locationRepository)
        every { carRepository.findOneById(any()) } returns CarEntity(
            ObjectId("64c8fb032eb57e0b2626907c"),
            LocationEntity(
                ObjectId("64d4b15e0632c87bd89d3512"),
                80,
                "Breite Strasse",
                50667,
                "Cologne"
            ),
            "Audi", "A4", 5, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0
        )

        // when
        val car = carService.getCarByID("64c8fb032eb57e0b2626907c")

        Assertions.assertThat(car!!.id).isEqualTo("64c8fb032eb57e0b2626907c")
    }

    @Test
    fun `getCarByIDThatDoenstExists() Should throw NotFoundException`() {
        // given
        val carService = CarService(carRepository, locationRepository)
        every { carRepository.findOneById(any()) } returns null

        // then
        assertThrows<NotFoundException> {
            // when
            carService.getCarByID("64c7a03aa6148808920a8ab1")
        }
    }
}
