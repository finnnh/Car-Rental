package com.finn.carrental.domain

import com.finn.carrental.api.dtos.UserRequest
import com.finn.carrental.domain.exceptions.AlreadyExistsException
import com.finn.carrental.domain.exceptions.NotFoundException
import com.finn.carrental.domain.models.User
import com.finn.carrental.persistence.UserRepository
import com.finn.carrental.persistence.entities.UserEntity
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DuplicateKeyException

class UserServiceTest {

    private val userRepository: UserRepository = mockk()

    @Test
    fun `createUser() Should add A User`() {
        // given
        val userService = UserService(userRepository)
        val userRequest = UserRequest("Finn", "Testname", "test@gmail.com")
        every { userRepository.save(any()) } returnsArgument(0)

        // when
        val user = userService.createUser(userRequest)

        // then
        val expectedUser = User("Finn", "", "Testname", "test@gmail.com")
        Assertions.assertThat(user).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedUser)
    }

    @Test
    fun `createUserThatExists() Should Throw AlreadyExistsException`() {
        // given
        val userService = UserService(userRepository)
        val userRequest = UserRequest("Duplicated User", "Testname", "test@gmail.com")

        every { userRepository.save(any()) } throws DuplicateKeyException("")

        // then
        assertThrows<AlreadyExistsException> {
            // when
            userService.createUser(userRequest)
        }
    }

    @Test
    fun `getAllUsers() Should return multiple User`() {
        // given
        val userService = UserService(userRepository)
        every { userRepository.findAll() } returns listOf(UserEntity(name = "Finn", lastname = "TestName", email = "test@gmail.com"), UserEntity(name = "Kostas", lastname = "TestName", email = "test@outlook.com"))

        // when
        val list = userService.getAllUsers()

        // then
        Assertions.assertThat(list).isNotEmpty()
    }

    @Test
    fun `getUserByID() Should return a User with the given ID`() {
        // given
        val userService = UserService(userRepository)
        every { userRepository.findOneById(ObjectId("64c7a03aa6148808920a8ab6")) } returns UserEntity(ObjectId("64c7a03aa6148808920a8ab6"), "Finn", "TestName", "test@gmail.com")

        // when
        val user = userService.getUserByID("64c7a03aa6148808920a8ab6")

        Assertions.assertThat(user!!.id).isEqualTo("64c7a03aa6148808920a8ab6")
    }

    @Test
    fun `getUserByIDThatDoenstExists() Should throw NotFoundException`() {
        // given
        val userService = UserService(userRepository)
        every { userRepository.findOneById(any()) } returns null

        // then
        assertThrows<NotFoundException> {
            // when
            userService.getUserByID("64c7a03aa6148808920a8ab1")
        }
    }
}
