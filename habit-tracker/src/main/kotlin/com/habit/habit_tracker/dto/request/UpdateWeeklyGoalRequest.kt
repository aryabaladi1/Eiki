package com.habit.habit_tracker.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class UpdateWeeklyGoalRequest(
    @field:Min(value = 0, message = "Weekly goal must be at least 0")
    @field:Max(value = 10080, message = "Weekly goal cannot exceed 10080 minutes (one week)")
    val weeklyGoal: Int? = null,

    @field:NotNull(message = "Start of the week is required")
    val weekStart: LocalDate,

    @field:NotNull(message = "End of the week is required")
    val weekEnd: LocalDate
)