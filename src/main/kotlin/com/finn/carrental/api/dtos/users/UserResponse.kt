package com.finn.carrental.api.dtos.users

import com.finn.carrental.domain.models.User
import io.swagger.v3.oas.annotations.media.Schema

class UserResponse(

    @Schema(example = "64ca230bbf69b338c2c47568")
    val id: String,

    @Schema(example = "Bob")
    val name: String,

    @Schema(example = "Doe")
    val lastname: String,

    @Schema(example = "bob.doe@gmail.com")
    val email: String
) {

    companion object {
        fun User.toDTO(): UserResponse {
            return UserResponse(this.id, this.name, this.lastname, this.email)
        }
    }
}
