package com.habit.habit_tracker.dto.request

import com.habit.habit_tracker.enums.TaskDifficulty

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class TaskCreateRequest(

    @field:NotBlank(message = "Task name cannot be empty")
    @field:Size(max = 50, message = "Task name cannot exceed 50 characters")
    val name: String,

    @field:Size(max = 200, message = "Description cannot exceed 200 characters")
    val description: String? = null,

    @field:NotNull(message = "Task difficulty is required")
    val difficulty: TaskDifficulty? = null,

    @field:Future(message = "Due date must be in the future")
    val dueDate: LocalDate? = null
)