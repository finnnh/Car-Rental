package com.finn.carrental.api

import com.finn.carrental.api.dtos.UserRequest
import com.finn.carrental.api.dtos.UserResponse
import com.finn.carrental.api.dtos.UserResponse.Companion.toDTO
import com.finn.carrental.domain.UserService
import com.finn.carrental.domain.exceptions.AlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @GetMapping("")
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(userService.getAllUsers().map { user -> user.toDTO() })
    }

    @GetMapping("{id}")
    fun getUserByID(@PathVariable("id") id: String): ResponseEntity<UserResponse> {
        val user = userService.getUserByID(id)?.toDTO() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

        return ResponseEntity(user, HttpStatus.OK)
    }

    @PostMapping("")
    fun createUser(@RequestBody request: UserRequest): ResponseEntity<UserResponse> {
        try {
            val user = userService.createUser(request)?.toDTO()
            return ResponseEntity(user, HttpStatus.CREATED)
        } catch (exception: AlreadyExistsException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with that Email already exists")
        }
    }
}
