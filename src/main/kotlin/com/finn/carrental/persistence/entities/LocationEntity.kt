package com.finn.carrental.persistence.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document("locations")
@TypeAlias("LocationEntity")
class LocationEntity (

    @Id
    var id: ObjectId = ObjectId.get(),
    var houseNumber: Int,
    var street: String,
    var postalCode: Int,
    var city: String

)
