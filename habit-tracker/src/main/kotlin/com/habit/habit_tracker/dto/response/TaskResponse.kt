package com.habit.habit_tracker.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.habit.habit_tracker.enums.TaskDifficulty
import com.habit.habit_tracker.enums.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TaskResponse(
    val id: Long,
    val userId: Long,
    val name: String,
    val description: String?,
    val status: TaskStatus,
    val difficulty: TaskDifficulty?,
    val dueDate: LocalDate?,
    val createdAt: LocalDateTime
)