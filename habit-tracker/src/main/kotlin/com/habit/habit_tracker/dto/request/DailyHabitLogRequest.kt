package com.habit.habit_tracker.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class DailyHabitLogRequest(
    @field:Min(value = 0, message = "Minutes done cannot be negative")
    @field:Max(value = 1440, message = "Minutes done cannot exceed 1440 per day")
    val minutesDone: Int? = null,

    @field:NotNull(message = "Date is required")
    val date: LocalDate
)