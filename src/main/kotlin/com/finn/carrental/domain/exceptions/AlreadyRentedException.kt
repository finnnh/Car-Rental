package com.finn.carrental.domain.exceptions

class AlreadyRentedException(message: String = "Car is already rented during that time period") : RuntimeException(message)
