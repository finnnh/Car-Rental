package com.finn.carrental.persistence.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document("cars")
@TypeAlias("CarEntity")
class CarEntity(
    @Id
    var id: ObjectId = ObjectId.get(),
    var brand: String,
    var model: String,
    var seats: Int
)