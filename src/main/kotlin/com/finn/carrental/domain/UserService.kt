package com.finn.carrental.domain

import com.finn.carrental.api.dtos.UserRequest
import com.finn.carrental.domain.exceptions.AlreadyExistsException
import com.finn.carrental.domain.models.User
import com.finn.carrental.domain.models.User.Companion.toDomain
import com.finn.carrental.persistence.UserRepository
import com.finn.carrental.persistence.entities.CarEntity
import com.finn.carrental.persistence.entities.UserEntity
import org.bson.types.ObjectId
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUser(): List<User> {
        return userRepository.findAll().map { userEntity -> userEntity.toDomain() }
    }

    fun getUserByID(id: String): User {
        return userRepository.findOneById(ObjectId(id)).toDomain()
    }

    fun createUser(userRequest: UserRequest): User {
        try {
            return userRepository.save(
                UserEntity(
                    name = userRequest.name,
                    lastname = userRequest.lastname,
                    email = userRequest.email,
                    cars = listOf()
                )).toDomain()
        } catch (exception: DuplicateKeyException) {
            throw AlreadyExistsException()
        }
    }
}
