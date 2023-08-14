package com.finn.carrental.domain

import com.finn.carrental.persistence.CarRepository
import com.finn.carrental.persistence.entities.CarEntity
import com.finn.carrental.persistence.entities.LocationEntity
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

class PriceCalculatorServiceTest {

    private val carRepository: CarRepository = mockk()

    @Test
    fun `getPrice30() Should return the Price`() {
        // given
        val priceCalculatorService = PriceCalculatorService(carRepository, "steps")

        every { carRepository.findOneById(any()) } returns
            CarEntity(
                ObjectId("64ca5e14df5d3d4585ad2fa2"),
                LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                "Audi",
                "A4",
                5,
                7.0,
                5.0,
                3.0,
                5.0,
                3.0,
                1.0
            )

        // when
        val result = priceCalculatorService.calculatePriceForCarSteps("64ca5e14df5d3d4585ad2fa2", 5, 30)

        // then
        Assertions.assertThat(result).isEqualTo(235.0)
    }

    @Test
    fun `getPrice70() Should return the Price`() {
        // given
        val priceCalculatorService = PriceCalculatorService(carRepository, "steps")

        every { carRepository.findOneById(any()) } returns
            CarEntity(
                ObjectId("64ca5e14df5d3d4585ad2fa2"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                "Audi",
                "A4",
                5,
                7.0,
                5.0,
                3.0,
                5.0,
                3.0,
                1.0
            )

        // when
        val result = priceCalculatorService.calculatePriceForCarSteps("64ca5e14df5d3d4585ad2fa2", 5, 70)

        // then
        Assertions.assertThat(result).isEqualTo(435.0)
    }

    @Test
    fun `getPrice100() Should return the Price`() {
        // given
        val priceCalculatorService = PriceCalculatorService(carRepository, "steps")

        every { carRepository.findOneById(any()) } returns
            CarEntity(
                ObjectId("64ca5e14df5d3d4585ad2fa2"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                "Audi",
                "A4",
                5,
                7.0,
                5.0,
                3.0,
                5.0,
                3.0,
                1.0
            )

        // when
        val result = priceCalculatorService.calculatePriceForCarSteps("64ca5e14df5d3d4585ad2fa2", 5, 100)

        // then
        Assertions.assertThat(result).isEqualTo(585.0)
    }

    @Test
    fun `getPrice10H() Should return the Price`() {
        // given
        val priceCalculatorService = PriceCalculatorService(carRepository, "steps")

        every { carRepository.findOneById(any()) } returns
            CarEntity(
                ObjectId("64ca5e14df5d3d4585ad2fa2"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                "Audi",
                "A4",
                5,
                7.0,
                5.0,
                3.0,
                5.0,
                3.0,
                1.0
            )

        // when
        val result = priceCalculatorService.calculatePriceForCarSteps("64ca5e14df5d3d4585ad2fa2", 10, 30)

        // then
        Assertions.assertThat(result).isEqualTo(252.0)
    }

    @Test
    fun `getPrice48H() Should return the Price`() {
        // given
        val priceCalculatorService = PriceCalculatorService(carRepository, "steps")

        every { carRepository.findOneById(any()) } returns
            CarEntity(
                ObjectId("64ca5e14df5d3d4585ad2fa2"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                "Audi",
                "A4",
                5,
                7.0,
                5.0,
                3.0,
                5.0,
                3.0,
                1.0
            )

        // when
        val result = priceCalculatorService.calculatePriceForCarSteps("64ca5e14df5d3d4585ad2fa2", 48, 30)

        // then
        Assertions.assertThat(result).isEqualTo(290.0)
    }

    @Test
    fun `getPrice50H() Should return the Price`() {
        // given
        val priceCalculatorService = PriceCalculatorService(carRepository, "steps")

        every { carRepository.findOneById(any()) } returns
            CarEntity(
                ObjectId("64ca5e14df5d3d4585ad2fa2"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                "Audi",
                "A4",
                5,
                7.0,
                5.0,
                3.0,
                5.0,
                3.0,
                1.0
            )

        // when
        val result = priceCalculatorService.calculatePriceForCarSteps("64ca5e14df5d3d4585ad2fa2", 50, 30)

        // then
        Assertions.assertThat(result).isEqualTo(292.0)
    }
}
