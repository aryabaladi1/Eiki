package com.habit.habit_tracker.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FullHabitLogsForWeekResponse(
    val dailyHabitLogs: List<DailyHabitLogResponse>,
    val weeklyHabitLog: WeeklyHabitLogResponse?,
    val habit: HabitResponse,
)