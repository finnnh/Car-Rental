package com.finn.carrental.persistence.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("users")
@TypeAlias("UserEntity")
data class UserEntity(
    @Id
    var id: ObjectId = ObjectId.get(),
    val name: String,
    val lastname: String,
    @Indexed(unique = true)
    val email: String,
)
