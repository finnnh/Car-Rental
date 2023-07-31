package com.finn.carrental.persistence

import com.finn.carrental.persistence.entities.UserEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface UserRepository : MongoRepository<UserEntity, String> {
    fun findOneById(id: ObjectId): UserEntity

    override fun deleteAll()
}