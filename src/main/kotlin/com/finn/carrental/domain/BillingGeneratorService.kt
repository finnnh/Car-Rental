package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.persistence.RentalRepository
import com.finn.carrental.persistence.UserRepository
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.PageSize
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.util.stream.Stream

@Service
class BillingGeneratorService(val userRepository: UserRepository, val rentalRepository: RentalRepository, val priceCalculatorService: PriceCalculatorService) {
    fun generateBillingPDF(userId: String, billingMonth: Int, billingYear: Int): ByteArray {
        val user = userRepository.findOneById(ObjectId(userId)) ?: throw NotFoundException()
        val rentals = rentalRepository.findByUserEntity(user)

        val document = Document()
        val byteArrayOutputStream = ByteArrayOutputStream()

        PdfWriter.getInstance(document, byteArrayOutputStream)

        document.addAuthor("Car-Rantal-Company")
        document.addCreationDate()
        document.addTitle("Billing")
        document.setPageSize(PageSize.LETTER)

        document.open()

        val table = PdfPTable(4)
        addTableHeader(table)
        var finalPrice = 0.0
        rentals.forEach {
            if (it.start.year == billingYear && it.start.month.value == billingMonth) {
                var pricePerHour = 0.0
                var pricePerKM = 0.0

                if (priceCalculatorService.pricingType == "linear") {
                    pricePerHour = priceCalculatorService.calculatePriceLinearHour(it.carEntity, it.hours)
                    pricePerKM = priceCalculatorService.calculatePriceLinearKM(it.carEntity, it.km)
                } else {
                    pricePerHour = priceCalculatorService.calculatePriceStepsHour(it.carEntity, it.hours)
                    pricePerKM = priceCalculatorService.calculatePriceStepsKM(it.carEntity, it.km)
                }

                val totalPrice = pricePerHour + pricePerKM

                addRow(
                    table,
                    it.carEntity.brand + "-" + it.carEntity.model,
                    pricePerKM.toString(),
                    pricePerHour.toString(),
                    totalPrice.toString()
                )
                finalPrice += totalPrice
            }
        }

        addRow(table, "", "", "", "")
        addRow(table, "Total", "", "", finalPrice.toString())

        document.add(table)
        document.close()

        return byteArrayOutputStream.toByteArray()
    }

    fun addTableHeader(table: PdfPTable) {
        Stream.of("Car", "Price for Hours", "Price for KM", "Total").forEach {
            val header = PdfPCell()
            header.backgroundColor = BaseColor.LIGHT_GRAY
            header.borderWidth = 2F
            header.phrase = Phrase(it)
            table.addCell(header)
        }
    }

    private fun addRow(table: PdfPTable, car: String, priceHour: String, priceKm: String, total: String) {
        table.addCell(car)
        table.addCell(priceHour)
        table.addCell(priceKm)
        table.addCell(total)
    }
}
