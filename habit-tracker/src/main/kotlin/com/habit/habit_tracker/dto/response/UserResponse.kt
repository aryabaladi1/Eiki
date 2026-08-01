package com.habit.habit_tracker.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponse(
    val id: Long,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val lastLogin: LocalDateTime?,
    val createdAt: LocalDateTime
)