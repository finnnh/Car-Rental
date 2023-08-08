package com.finn.carrental.domain.models

import com.finn.carrental.persistence.entities.UserEntity

class User(val id: String, val name: String, val lastname: String, val email: String) {

    companion object {
        fun UserEntity.toDomain(): User {
            return User(this.id.toString(), this.name, this.lastname, this.email)
        }
    }
}
