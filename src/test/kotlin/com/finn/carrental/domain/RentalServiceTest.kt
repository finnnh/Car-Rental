package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.AlreadyRentedException
import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.Car
import com.finn.carrental.domain.models.Location
import com.finn.carrental.domain.models.Rental
import com.finn.carrental.domain.models.User
import com.finn.carrental.persistence.CarRepository
import com.finn.carrental.persistence.LocationRepository
import com.finn.carrental.persistence.RentalRepository
import com.finn.carrental.persistence.UserRepository
import com.finn.carrental.persistence.entities.CarEntity
import com.finn.carrental.persistence.entities.LocationEntity
import com.finn.carrental.persistence.entities.RentalEntity
import com.finn.carrental.persistence.entities.UserEntity
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class RentalServiceTest {

    private val rentalRepository: RentalRepository = mockk()
    private val carRepository: CarRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val locationRepository: LocationRepository = mockk()

    @Test
    fun `createRental() Should add A Rental`() {
        // given
        val rentalService = RentalService(rentalRepository, userRepository, carRepository, locationRepository)
        every { rentalRepository.save(any()) } returnsArgument(0)
        every { carRepository.findOneById(any()) } returns CarEntity(ObjectId("64c8fb032eb57e0b2626907c"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"), "Audi", "A5", 5, 5.5, 7.5, 5.0, 5.0, 5.0, 5.0)
        every { userRepository.findOneById(any()) } returns UserEntity(ObjectId("64c8c410bebeef1000d78c80"), "Finn", "Hoffmann", "fihoffmann@web.de")
        every { rentalRepository.findByCarEntity(any()) } returns emptyList()
        every { locationRepository.findOneById(any()) } returns LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne")

        // when
        val rental = rentalService.createRental(
            "64c8c410bebeef1000d78c80",
            "64c8fb032eb57e0b2626907c",
            LocalDateTime.of(2023, 8, 20, 12, 0),
            LocalDateTime.of(2023, 8, 25, 12, 0),
            48,
            300,
            "64d4b15e0632c87bd89d3512",
            "64d4b15e0632c87bd89d3512"
        )

        // then
        val expectedRental = Rental(
            "64c8c410bebeef1000d78c80",
            User("64c8c410bebeef1000d78c80", "Finn", "Hoffmann", "fihoffmann@web.de"),
            Car(
                "64c8fb032eb57e0b2626907c",
                Location(
                    "64c8c410bebeef1000d78c80",
                    80,
                    "Breite Strasse",
                    50667,
                    "Cologne"
                ),
                "Audi", "A5", 5, 5.5, 7.5, 5.0, 5.0, 5.0, 5.0
            ),
            LocalDateTime.of(2023, 8, 20, 12, 0),
            LocalDateTime.of(2023, 8, 25, 12, 0),
            48,
            300,
            Location("64d4b15e0632c87bd89d3512", 2, "Im Zollhafen", 50678, "Cologne"),
            Location("64d4b15e0632c87bd89d3512", 2, "Im Zollhafen", 50678, "Cologne")
        )

        Assertions.assertThat(rental).usingRecursiveComparison().ignoringFields("id", "pricePerDistanceHigh", "pricePerDistanceModerate", "pricePerDistanceLow", "pricePerHourHigh", "pricePerHourModerate", "pricePerHourLow", "car").isEqualTo(expectedRental)
    }

    @Test
    fun `createRentalInABlockedTimeperiod() Should Throw an AlreadyRentedException`() {
        // given
        val rentalService = RentalService(rentalRepository, userRepository, carRepository, locationRepository)
        every { rentalRepository.save(any()) } returnsArgument(0)
        every { carRepository.findOneById(any()) } returns CarEntity(ObjectId("64c8fb032eb57e0b2626907c"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"), "Audi", "A5", 5, 5.5, 7.5, 5.0, 5.0, 5.0, 5.0)
        every { userRepository.findOneById(any()) } returns UserEntity(ObjectId("64c8c410bebeef1000d78c80"), "Finn", "Hoffmann", "fihoffmann@web.de")
        every { rentalRepository.findByCarEntity(any()) } returns listOf(
            RentalEntity(
                ObjectId("64c8fb032eb57e0b2626909c"),
                UserEntity(ObjectId("64c8c410bebeef1000d78c80"), "Finn", "Hoffmann", "fihoffmann@web.de"),
                CarEntity(ObjectId("64c8fb032eb57e0b2626907c"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"), "Audi", "A4", 5, 5.5, 7.5, 5.0, 5.0, 5.0, 5.0),
                LocalDateTime.of(2023, 8, 20, 12, 0),
                LocalDateTime.of(2023, 8, 25, 12, 0),
                48,
                300,
                LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne")
            )
        )

        // then
        assertThrows<AlreadyRentedException> {
            // when
            rentalService.createRental(
                "64c8c410bebeef1000d78c80",
                "64c8fb032eb57e0b2626907c",
                LocalDateTime.of(2023, 8, 21, 12, 0),
                LocalDateTime.of(2023, 8, 23, 12, 0),
                48,
                300,
                "64d4b15e0632c87bd89d3512",
                "64d4b15e0632c87bd89d3512"
            )
        }
    }

    @Test
    fun `getAllRentals() Should return multiple Rentals`() {
        // given
        val rentalService = RentalService(rentalRepository, userRepository, carRepository, locationRepository)
        every { rentalRepository.findAll() } returns listOf(
            RentalEntity(
                ObjectId("64c8fb032eb57e0b2626909c"),
                UserEntity(ObjectId("64c8c410bebeef1000d78c80"), "Finn", "Hoffmann", "fihoffmann@web.de"),
                CarEntity(ObjectId("64c8fb032eb57e0b2626907c"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"), "Audi", "A4", 5, 5.5, 7.5, 5.0, 5.0, 5.0, 5.0),
                LocalDateTime.of(2023, 8, 20, 12, 0),
                LocalDateTime.of(2023, 8, 25, 12, 0),
                48,
                300,
                LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne")
            ),

            RentalEntity(
                ObjectId("64c8fb032eb57e0b2626901c"),
                UserEntity(ObjectId("64c8c410bebeef1000d78c80"), "Kostas", "Lastname", "test@web.de"),
                CarEntity(ObjectId("64c8fb032eb57e0b2626907c"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"), "Audi", "A4", 5, 5.5, 7.5, 5.0, 5.0, 5.0, 5.0),
                LocalDateTime.of(2023, 8, 10, 12, 0),
                LocalDateTime.of(2023, 8, 15, 12, 0),
                48,
                300,
                LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne")
            )
        )

        // when
        val list = rentalService.getAllRentals()

        // then
        Assertions.assertThat(list).isNotEmpty()
    }

    @Test
    fun `getRentalByID() Should return a Rental with the given ID`() {
        // given
        val rentalService = RentalService(rentalRepository, userRepository, carRepository, locationRepository)
        every { rentalRepository.findOneById(any()) } returns RentalEntity(
            ObjectId("64c8fb032eb57e0b2626909c"),
            UserEntity(ObjectId("64c8c410bebeef1000d78c80"), "Finn", "Hoffmann", "fihoffmann@web.de"),
            CarEntity(ObjectId("64c8fb032eb57e0b2626907c"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"), "Audi", "A4", 5, 5.5, 7.5, 5.0, 5.0, 5.0, 5.0),
            LocalDateTime.of(2023, 8, 20, 12, 0),
            LocalDateTime.of(2023, 8, 25, 12, 0),
            48,
            300,
            LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
            LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne")
        )

        // when
        val rental = rentalService.getRentalById("64c8fb032eb57e0b2626909c")

        Assertions.assertThat(rental!!.id).isEqualTo("64c8fb032eb57e0b2626909c")
    }

    @Test
    fun `getRentalByIDThatDoenstExists() Should throw NotFoundException`() {
        // given
        val rentalService = RentalService(rentalRepository, userRepository, carRepository, locationRepository)
        every { rentalRepository.findOneById(any()) } returns null

        // then
        assertThrows<NotFoundException> {
            // when
            rentalService.getRentalById("64c7a03aa6148808920a8ab1")
        }
    }
}
