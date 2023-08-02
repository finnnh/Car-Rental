package com.finn.carrental.persistence.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.LocalDateTime

@Document("rentals")
@TypeAlias("RentalEntity")
class RentalEntity(
    @Id
    var id: ObjectId = ObjectId.get(),
    @DocumentReference
    var userEntity: UserEntity,
    @DocumentReference
    var carEntity: CarEntity,
    var start: LocalDateTime = LocalDateTime.now(),
    var end: LocalDateTime
)
