package com.finn.carrental.api.dtos

import com.finn.carrental.domain.models.User
import io.swagger.v3.oas.annotations.media.Schema

class UserResponse(

    @Schema(example = "Bob")
    val name: String,

    @Schema(example = "64ca230bbf69b338c2c47568")
    val id: String,

    @Schema(example = "Doe")
    val lastname: String,

    @Schema(example = "bob.doe@gmail.com")
    val email: String
) {

    companion object {
        fun User.toDTO(): UserResponse {
            return UserResponse(this.name, this.id, this.lastname, this.email)
        }
    }
}
