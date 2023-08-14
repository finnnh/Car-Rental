package com.finn.carrental.persistence.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("cars")
@TypeAlias("CarEntity")
class CarEntity(

    @Id
    var id: ObjectId = ObjectId.get(),
    @DocumentReference
    var locationEntity: LocationEntity,
    var brand: String,
    var model: String,
    var seats: Int,
    var pricePerDistanceHigh: Double,
    var pricePerDistanceModerate: Double,
    var pricePerDistanceLow: Double,
    var pricePerHourHigh: Double,
    var pricePerHourModerate: Double,
    var pricePerHourLow: Double
)
