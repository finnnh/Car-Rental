package com.finn.carrental.domain

import com.finn.carrental.persistence.RentalRepository
import com.finn.carrental.persistence.UserRepository
import com.finn.carrental.persistence.entities.CarEntity
import com.finn.carrental.persistence.entities.LocationEntity
import com.finn.carrental.persistence.entities.RentalEntity
import com.finn.carrental.persistence.entities.UserEntity
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime

class BillingGeneratorServiceTest {

    private val userRepository: UserRepository = mockk()
    private val rentalRepository: RentalRepository = mockk()
    private val priceCalculatorService: PriceCalculatorService = mockk()

    @Test
    fun `generatePDF() Should Return Bytes()`() {
        // given
        val document = Document()
        val billingGeneratorService = BillingGeneratorService(userRepository, rentalRepository, priceCalculatorService)

        val userEntity = UserEntity(ObjectId("64c7a03aa6148808920a8ab6"), "Finn", "TestName", "test@gmail.com")
        val carEntity = CarEntity(
            ObjectId("64c7a03aa6148808920a8ab6"), LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
            "Audi", "A4", 5, 7.5, 5.5, 3.5, 5.5, 3.5, 1.5
        )
        val rentalEntity = RentalEntity(
            ObjectId("64c7a03aa6148808920a8ab6"), userEntity, carEntity, LocalDateTime.of(2023, 8, 2, 12, 0), LocalDateTime.of(2023, 8, 5, 12, 0), 12, 300,
            LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
            LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne")
        )

        val list = listOf(
            RentalEntity(
                ObjectId("64c7a03aa6148808920a8ab6"),
                userEntity,
                carEntity,
                LocalDateTime.of(2023, 8, 2, 12, 0),
                LocalDateTime.of(2023, 8, 5, 12, 0),
                12,
                300,
                LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne"),
                LocationEntity(ObjectId("64d4b15e0632c87bd89d3512"), 2, "Im Zollhafen", 50678, "Cologne")
            )
        )

        every { userRepository.findOneById(any()) } returns userEntity
        every { rentalRepository.findOneById(any()) } returns rentalEntity
        every { rentalRepository.findByUserEntity(any()) } returns list
        every { priceCalculatorService.pricingType } returns "steps"
        every { priceCalculatorService.calculatePriceStepsKM(any(), any()) } returns 700.0
        every { priceCalculatorService.calculatePriceStepsHour(any(), any()) } returns 400.0

        val byteArrayOutputStream = ByteArrayOutputStream()
        PdfWriter.getInstance(document, byteArrayOutputStream)
        document.open()
        val content = Chunk("Test")
        document.add(content)
        document.close()

        billingGeneratorService.generateBillingPDF("64c7a03aa6148808920a8ab6", 8, 2023)

        // when
        val pfdByte = billingGeneratorService.generateBillingPDF("64c7a03aa6148808920a8ab6", 8, 2023)

        Assertions.assertThat(pfdByte.size).isGreaterThan(0)
    }
}
