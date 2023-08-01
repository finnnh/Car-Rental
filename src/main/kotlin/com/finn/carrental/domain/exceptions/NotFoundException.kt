package com.finn.carrental.domain.exceptions;

class NotFoundException(message: String = "Resource not found") : RuntimeException(message)
