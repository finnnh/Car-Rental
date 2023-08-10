package com.finn.carrental.persistence

import com.finn.carrental.persistence.entities.LocationEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface LocationRepository : MongoRepository<LocationEntity, String> {
    fun findOneById(id: ObjectId): LocationEntity?
    fun findOneByCity(city: String): LocationEntity?

    override fun deleteAll()

}