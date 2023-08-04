package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.persistence.CarRepository
import com.finn.carrental.persistence.RentalRepository
import com.finn.carrental.persistence.UserRepository
import com.finn.carrental.persistence.entities.UserEntity
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import jakarta.servlet.http.HttpServletResponse
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.stream.Stream


@Service
class BillingGeneratorService(val userRepository: UserRepository, val carRepository: CarRepository, val rentalRepository: RentalRepository) {
    fun generateBillingPDF(response: HttpServletResponse, userId: String, month: Int, year: Int) {
        val user = userRepository.findOneById(ObjectId(userId)) ?: throw NotFoundException()
        val rentals = rentalRepository.findByUserEntity(user)

        val document = Document()
        PdfWriter.getInstance(document, response.outputStream)

        document.open()
        val font = FontFactory.getFont(FontFactory.COURIER)
        val chunk = Chunk("Test", font)
        document.add(chunk)

        val table = PdfPTable(4)

        addTableHeader(table)

        println(rentals.size)

        rentals.forEach {
            addRows(table, it.carEntity.brand + it.carEntity.model, it.carEntity.pricePerDistanceHigh, it.carEntity.pricePerHourHigh)
        }

        document.add(table)

        document.close()
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

    private fun addRows(table: PdfPTable, car: String, priceHour: Double, priceKm: Double) {
        table.addCell(car)
        table.addCell(priceHour.toString())
        table.addCell(priceKm.toString())
        table.addCell((priceKm + priceHour).toString())
    }

}