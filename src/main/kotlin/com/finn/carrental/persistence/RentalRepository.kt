package com.finn.carrental.persistence

import com.finn.carrental.persistence.entities.CarEntity
import com.finn.carrental.persistence.entities.RentalEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RentalRepository : MongoRepository<RentalEntity, String> {

    fun findOneById(id: ObjectId): RentalEntity?

    fun findByCarEntity(entity: CarEntity): List<RentalEntity>

    override fun deleteAll()
}
