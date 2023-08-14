package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.Location
import com.finn.carrental.persistence.LocationRepository
import com.finn.carrental.persistence.entities.LocationEntity
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LocationServiceTest {

    private val locationRepository: LocationRepository = mockk()

    @Test
    fun `createLocation() Should add a Location`() {
        // given
        val locationService = LocationService(locationRepository)
        every { locationRepository.save(any()) } returnsArgument(0)

        // when
        val location = locationService.createLocation(2, "Im Zollhafen", 50678, "Cologne")

        // then
        val expectedLocation = Location("", 2, "Im Zollhafen", 50678, "Cologne")
        Assertions.assertThat(location).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedLocation)
    }

    @Test
    fun `getAllCars() Should return multiple Locations`() {
        val locationService = LocationService(locationRepository)
        every { locationRepository.findAll() } returns listOf(LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"))

        // when
        val list = locationService.getAllLocations()

        // then
        Assertions.assertThat(list).isNotEmpty()
    }

    @Test
    fun `getLocationByID() Should return a Location with the given ID`() {
        // given
        val locationService = LocationService(locationRepository)
        every { locationRepository.findOneById(any()) } returns LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne")

        // when
        val location = locationService.getLocationById("64d4b15e0632c87bd89d3512")

        Assertions.assertThat(location!!.id).isEqualTo("64d4b15e0632c87bd89d3512")
    }

    @Test
    fun `getLocationByCIty() Should return a Location with the given City`() {
        // given
        val locationService = LocationService(locationRepository)
        every { locationRepository.findByCity(any()) } returns listOf(
            LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
            LocationEntity(ObjectId("64d4b1a60632c87bd89d3513"), 80, "Breite Strasse", 50667, "Cologne")
        )

        // when
        val locations = locationService.getLocationsByCity("64d4b15e0632c87bd89d3512")

        Assertions.assertThat(locations).isNotEmpty()
    }

    @Test
    fun `getLocationByIDThatDoenstExists() Should throw NotFoundException`() {
        // given
        val locationService = LocationService(locationRepository)
        every { locationRepository.findOneById(any()) } returns null

        // then
        assertThrows<NotFoundException> {
            // when
            locationService.getLocationById("64c7a03aa6148808920a8ab1")
        }
    }
}
