package com.finn.carrental.api.dtos

import java.time.LocalDateTime

class RentalRequest(val userId: String, val carId: String, val start: LocalDateTime, val end: LocalDateTime)
