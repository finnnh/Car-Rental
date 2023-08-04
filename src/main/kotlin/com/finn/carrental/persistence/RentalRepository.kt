package com.finn.carrental.persistence

import com.finn.carrental.persistence.entities.CarEntity
import com.finn.carrental.persistence.entities.RentalEntity
import com.finn.carrental.persistence.entities.UserEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RentalRepository : MongoRepository<RentalEntity, String> {

    fun findOneById(id: ObjectId): RentalEntity?

    fun findByCarEntity(entity: CarEntity): List<RentalEntity>

    fun findByUserEntity(entity: UserEntity): List<RentalEntity>

    override fun deleteAll()
}
