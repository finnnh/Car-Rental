package com.finn.carrental.persistence

import com.finn.carrental.persistence.entities.CarEntity
import com.finn.carrental.persistence.entities.LocationEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface CarRepository : MongoRepository<CarEntity, String> {
    fun findOneById(id: ObjectId): CarEntity?

    fun findByLocationEntity(locationEntity: LocationEntity): List<LocationEntity>

    override fun deleteAll()
}
