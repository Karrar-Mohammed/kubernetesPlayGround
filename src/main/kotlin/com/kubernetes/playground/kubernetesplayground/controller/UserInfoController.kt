package com.kubernetes.playground.kubernetesplayground.controller

import com.kubernetes.playground.kubernetesplayground.dto.UserInfoRequest
import com.kubernetes.playground.kubernetesplayground.dto.UserInfoResponse
import com.kubernetes.playground.kubernetesplayground.service.UserInfoService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserInfoController(
    private val userService: UserInfoService
) {
    private val logger = LoggerFactory.getLogger(UserInfoController::class.java)

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserInfoResponse>> {
        logger.info("REST Request: GET /api/users")
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserInfoResponse> {
        logger.info("REST Request: GET /api/users/{}", id)
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @PostMapping
    fun createUser(@Valid @RequestBody request: UserInfoRequest): ResponseEntity<UserInfoResponse> {
        logger.info("REST Request: POST /api/users - Payload received: {}", request)
        val created = userService.createUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserInfoRequest
    ): ResponseEntity<UserInfoResponse> {
        logger.info("REST Request: PUT /api/users/{} - Payload received: {}", id, request)
        val updated = userService.updateUser(id, request)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        logger.info("REST Request: DELETE /api/users/{}", id)
        userService.deleteUser(id)
        return ResponseEntity.ok(mapOf("message" to "User with ID $id deleted successfully"))
    }
}
