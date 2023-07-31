package com.finn.carrental

import com.finn.carrental.persistence.UserRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class CarRentalApplication

fun main(args: Array<String>) {
	runApplication<CarRentalApplication>(*args)
}
