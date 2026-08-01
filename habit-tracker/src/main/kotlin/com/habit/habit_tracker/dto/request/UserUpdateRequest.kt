package com.habit.habit_tracker.dto.request

import jakarta.validation.constraints.Size

data class UserUpdateRequest(

    @field:Size(min = 3, max = 50)
    val username: String? = null,

    @field:Size(max = 50)
    val firstName: String? = null,

    @field:Size(max = 50)
    val lastName: String? = null
)