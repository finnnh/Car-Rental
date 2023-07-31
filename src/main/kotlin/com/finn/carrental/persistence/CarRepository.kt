package com.finn.carrental.persistence

import com.finn.carrental.persistence.entities.CarEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface CarRepository : MongoRepository<CarEntity, String> {
    fun findOneById(id: ObjectId): CarEntity

    override fun deleteAll()
}