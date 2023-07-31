package com.finn.carrental.api

import com.finn.carrental.domain.UserService
import com.finn.carrental.api.dtos.UserRequest
import com.finn.carrental.api.dtos.UserResponse
import com.finn.carrental.api.dtos.UserResponse.Companion.toDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val userService: UserService) {

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(userService.getAllUserByID().map { user -> user.toDTO() })
    }

    @GetMapping("/users/{id}")
    fun getUserByID(@PathVariable("id") id: String): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(userService.getUserByID(id).toDTO())
    }

    @PostMapping("/users")
    fun createUser(@RequestBody request: UserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.createUser(request).toDTO(), HttpStatus.CREATED)
    }

}