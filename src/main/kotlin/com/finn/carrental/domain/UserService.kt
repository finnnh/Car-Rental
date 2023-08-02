package com.finn.carrental.domain

import com.finn.carrental.domain.exceptions.AlreadyExistsException
import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.User
import com.finn.carrental.domain.models.User.Companion.toDomain
import com.finn.carrental.persistence.UserRepository
import com.finn.carrental.persistence.entities.UserEntity
import org.bson.types.ObjectId
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll().map { userEntity -> userEntity.toDomain() }
    }

    fun getUserByID(id: String): User? {
        return userRepository.findOneById(ObjectId(id))?.toDomain() ?: throw NotFoundException()
    }

    /**
     * @param name The name of the User
     * @param lastname The name of the User
     * @param email The email of the User (Needs to be Unique)
     * @return The newly created User
     * @throws AlreadyExistsException if there is already a User with the same E-Mail
     */
    fun createUser(name: String, lastname: String, email: String): User? {
        try {
            return userRepository.save(
                UserEntity(
                    name = name,
                    lastname = lastname,
                    email = email
                )
            ).toDomain()
        } catch (exception: DuplicateKeyException) {
            throw AlreadyExistsException()
        }
    }
}
