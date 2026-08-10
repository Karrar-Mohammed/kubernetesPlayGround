package com.kubernetes.playground.kubernetesplayground.dto

import com.kubernetes.playground.kubernetesplayground.model.Gender
import jakarta.validation.constraints.*

data class UserInfoRequest(
    @field:NotNull(message = "Age is required")
    @field:Min(value = 0, message = "Age must be a non-negative integer (minimum 0)")
    @field:Max(value = 150, message = "Age cannot exceed 150")
    val age: Int?,

    @field:NotBlank(message = "First name is required and cannot be empty")
    @field:Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    val firstName: String?,

    @field:NotBlank(message = "Last name is required and cannot be empty")
    @field:Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    val lastName: String?,

    @field:NotBlank(message = "Place of birth is required and cannot be empty")
    @field:Size(min = 2, max = 100, message = "Place of birth must be between 2 and 100 characters")
    val placeOfBirth: String?,

    @field:NotBlank(message = "Address is required and cannot be empty")
    @field:Size(min = 5, max = 250, message = "Address must be between 5 and 250 characters")
    val address: String?,

    @field:NotNull(message = "Gender is required and must be either 'Male' or 'Female'")
    val gender: Gender?
)
