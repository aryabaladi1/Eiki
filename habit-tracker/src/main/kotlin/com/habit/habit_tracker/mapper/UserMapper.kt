package com.habit.habit_tracker.mapper

import com.habit.habit_tracker.domain.User
import com.habit.habit_tracker.dto.response.UserResponse

object UserMapper {
    fun toUserDetailsResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id!!,
            username = user.username,
            firstName = user.firstName,
            lastName = user.lastName,
            lastLogin = user.lastLogin,
            createdAt = user.createdAt!!
        )
    }
}
