package com.habit.habit_tracker.dto.request

import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 - 50 characters")
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(max = 100, message = "Password must be at most 100 characters")
    val password: String
)