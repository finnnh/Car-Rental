package com.finn.carrental.api.dtos.users

import io.swagger.v3.oas.annotations.media.Schema

class UserRequest(

    @Schema(example = "Bob")
    val name: String,

    @Schema(example = "Doe")
    val lastname: String,

    @Schema(example = "bob.doe@gmail.com")
    val email: String
)
