package com.finn.carrental.api

import com.finn.carrental.api.dtos.users.UserRequest
import com.finn.carrental.api.dtos.users.UserResponse
import com.finn.carrental.api.dtos.users.UserResponse.Companion.toDTO
import com.finn.carrental.domain.UserService
import com.finn.carrental.domain.exceptions.AlreadyExistsException
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
class UserController(val userService: UserService) {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful")
        ]
    )
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(userService.getAllUsers().map { user -> user.toDTO() })
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
            ApiResponse(responseCode = "404", description = "No User with this ID")
        ]
    )
    fun getUserByID(@PathVariable("id") id: String): ResponseEntity<UserResponse> {
        val user = userService.getUserByID(id)?.toDTO() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

        return ResponseEntity(user, HttpStatus.OK)
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
            ApiResponse(responseCode = "409", description = "There is a User with the give E-mail")
        ]
    )
    fun createUser(@RequestBody request: UserRequest): ResponseEntity<UserResponse> {
        try {
            val user = userService.createUser(request.name, request.lastname, request.email)?.toDTO()
            return ResponseEntity(user, HttpStatus.CREATED)
        } catch (exception: AlreadyExistsException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with that Email already exists")
        }
    }
}
