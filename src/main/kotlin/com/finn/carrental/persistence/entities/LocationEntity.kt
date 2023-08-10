package com.finn.carrental.persistence.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document("locations")
@TypeAlias("LocationEntity")
class LocationEntity (

    @Id
    val id: ObjectId = ObjectId.get(),
    val houseNumber: Int,
    val street: String,
    val postalCode: Int,
    val city: String

)
