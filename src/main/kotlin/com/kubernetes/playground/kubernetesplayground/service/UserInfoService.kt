package com.kubernetes.playground.kubernetesplayground.service

import com.kubernetes.playground.kubernetesplayground.dto.UserInfoRequest
import com.kubernetes.playground.kubernetesplayground.dto.UserInfoResponse
import com.kubernetes.playground.kubernetesplayground.model.UserInfo
import com.kubernetes.playground.kubernetesplayground.repository.UserInfoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserInfoService(
    private val userRepository: UserInfoRepository
) {
    private val logger = LoggerFactory.getLogger(UserInfoService::class.java)

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserInfoResponse> {
        logger.info("Fetching all user records from database")
        return userRepository.findAll().map { UserInfoResponse.fromEntity(it) }
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserInfoResponse {
        logger.info("Fetching user record with id: {}", id)
        val user = userRepository.findById(id).orElseThrow {
            logger.warn("User record with id {} not found", id)
            NoSuchElementException("User with ID $id not found")
        }
        return UserInfoResponse.fromEntity(user)
    }

    @Transactional
    fun createUser(request: UserInfoRequest): UserInfoResponse {
        logger.info("Creating new user: {} {}", request.firstName, request.lastName)
        val userInfo = UserInfo(
            age = request.age!!,
            firstName = request.firstName!!,
            lastName = request.lastName!!,
            placeOfBirth = request.placeOfBirth!!,
            address = request.address!!,
            gender = request.gender!!
        )
        val saved = userRepository.save(userInfo)
        logger.info("User created successfully with ID: {}", saved.id)
        return UserInfoResponse.fromEntity(saved)
    }

    @Transactional
    fun updateUser(id: Long, request: UserInfoRequest): UserInfoResponse {
        logger.info("Updating user record with ID: {}", id)
        val existingUser = userRepository.findById(id).orElseThrow {
            logger.warn("Cannot update. User record with id {} not found", id)
            NoSuchElementException("User with ID $id not found")
        }

        existingUser.age = request.age!!
        existingUser.firstName = request.firstName!!
        existingUser.lastName = request.lastName!!
        existingUser.placeOfBirth = request.placeOfBirth!!
        existingUser.address = request.address!!
        existingUser.gender = request.gender!!

        val updated = userRepository.save(existingUser)
        logger.info("User ID {} updated successfully", id)
        return UserInfoResponse.fromEntity(updated)
    }

    @Transactional
    fun deleteUser(id: Long) {
        logger.info("Deleting user record with ID: {}", id)
        if (!userRepository.existsById(id)) {
            logger.warn("Cannot delete. User record with id {} not found", id)
            throw NoSuchElementException("User with ID $id not found")
        }
        userRepository.deleteById(id)
        logger.info("User record with ID {} deleted successfully", id)
    }
}
