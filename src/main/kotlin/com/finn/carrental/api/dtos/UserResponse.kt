package com.finn.carrental.api.dtos

import com.finn.carrental.domain.models.User

class UserResponse(val name: String, val id: String, val lastname: String, val email: String) {

    companion object {
        fun User.toDTO(): UserResponse {
            return UserResponse(this.name, this.id, this.lastname, this.email)
        }
    }
}
