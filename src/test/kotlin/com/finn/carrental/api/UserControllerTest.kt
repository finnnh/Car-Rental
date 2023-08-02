package com.finn.carrental.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.finn.carrental.api.dtos.UserRequest
import com.finn.carrental.domain.UserService
import com.finn.carrental.domain.exceptions.AlreadyExistsException
import com.finn.carrental.domain.models.User
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class UserControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var userService: UserService

    private val mapper = jacksonObjectMapper()

    @Test
    fun `getUserByID() Should Return a User with ID 64c8c410bebeef1000d78c80`() {
        every { userService.getUserByID(any())} returns User("Finn", "64c8c410bebeef1000d78c80", "Testname", "test@gmail.com")

        mockMvc.perform(MockMvcRequestBuilders.get("/users/64c8c410bebeef1000d78c80"))
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id").value("64c8c410bebeef1000d78c80"))
    }

    @Test
    fun `getUserByIDThatDoenstExist() Should throw a Response Status Exception (NOT FOUND)`() {
        every { userService.getUserByID(any()) } returns null

        mockMvc.perform(MockMvcRequestBuilders.get("/users/64c8c410bebeef1000d78c80"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `getAllUsers() Should return a list of Users`() {
        every { userService.getAllUsers() } returns listOf(User("Finn", "64c8c410bebeef1000d78c80", "Lastname", "test@gmail.com"), User("Kostas", "64c8c410bebeef1000d78c81", "Lastname", "test1@gmail.com"))

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
            .andExpect(status().isOk)
            .andReturn()

        val notes: List<User> = mapper.readValue(result.response.contentAsString)

        Assertions.assertThat(notes).isNotEmpty
    }

    @Test
    fun `createUser() Should return the created User & Created Status`() {
        every { userService.createUser(any(), any(), any()) } returns User("Finn", "64c8c410bebeef1000d78c80", "Lastname", "test@gmail.com")

        val user = UserRequest("Finn", "Lastname", "test@gmail.com")

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val writer = mapper.writer().withDefaultPrettyPrinter()

        val requestJson = writer.writeValueAsString(user)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
            status().isCreated
        )
    }

    @Test
    fun `createUserThatAlreadyExist() Should have HttpStatus Conflict`() {
        every { userService.createUser(any(), any(), any()) } throws AlreadyExistsException()

        val user = UserRequest("Finn", "Lastname", "test@gmail.com")

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        val writer = mapper.writer().withDefaultPrettyPrinter()

        val requestJson = writer.writeValueAsString(user)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
            status().isConflict
        )
    }
}
