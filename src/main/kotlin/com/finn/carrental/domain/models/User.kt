package com.finn.carrental.domain.models

import com.finn.carrental.persistence.entities.UserEntity
import org.bson.types.ObjectId

class User(val name: String, val id: String, val lastname: String, val email: String) {

    companion object {
        fun UserEntity.toDomain(): User {
            return User(this.name, this.id.toString(), this.lastname, this.email)
        }
    }


}