package com.habit.habit_tracker.mapper

import com.habit.habit_tracker.domain.DailyHabitLog
import com.habit.habit_tracker.domain.WeeklyHabitLog
import com.habit.habit_tracker.domain.Habit

import com.habit.habit_tracker.dto.response.FullHabitLogsForWeekResponse


object FullHabitLogsForWeekMapper {

    fun toFullHabitLogsForWeek(
        d: List<DailyHabitLog>,
        w: WeeklyHabitLog?,
        h: Habit
    ): FullHabitLogsForWeekResponse {

        val dailyHabitLogs =
            d.map {
                DailyHabitLogMapper.toDailyHabitLogResponse(it)
            }

        val minutesDone =
            d.sumOf { it.minutesDone }

        val weeklyHabitLogResponse =
            WeeklyHabitLogStatsMapper.toWeeklyHabitLogResponse(
                w,
                minutesDone
            )

        val habitResponse =
            HabitMapper.toHabitResponse(h)

        return FullHabitLogsForWeekResponse(
            dailyHabitLogs,
            weeklyHabitLogResponse,
            habitResponse
        )
    }
}