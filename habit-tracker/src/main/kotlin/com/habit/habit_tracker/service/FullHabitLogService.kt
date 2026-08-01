package com.habit.habit_tracker.service

import org.springframework.stereotype.Service
import com.habit.habit_tracker.dto.response.FullHabitLogsForWeekResponse
import com.habit.habit_tracker.mapper.FullHabitLogsForWeekMapper
import com.habit.habit_tracker.repository.DailyHabitLogRepository
import com.habit.habit_tracker.repository.HabitRepository
import com.habit.habit_tracker.repository.WeeklyHabitLogRepository
import com.habit.habit_tracker.security.AuthUtil
import java.time.LocalDate

@Service
class FullHabitLogService(
    private val habitRepository: HabitRepository,
    private val dailyHabitLogRepository: DailyHabitLogRepository,
    private val weeklyHabitLogRepository: WeeklyHabitLogRepository,
    private val authUtil: AuthUtil
) {
    fun getFullHabitLogsForWeek(weekStart: LocalDate, weekEnd: LocalDate): List<FullHabitLogsForWeekResponse> {
        val user = authUtil.getAuthenticatedUser()

        val habits = habitRepository.findAllByUserId(user.id!!)

        val dailyLogs =
            dailyHabitLogRepository.findAllForUserWeek(
                user.id!!,
                weekStart,
                weekEnd
            )

        val weeklyLogs =
            weeklyHabitLogRepository.findAllForUserWeek(
                user.id!!,
                weekStart,
                weekEnd
            )


        val dailyByHabit = dailyLogs.groupBy { it.habit.id!! }
        val weeklyByHabit = weeklyLogs.associateBy { it.habit.id!! }

        val visibleHabits = habits.filter { habit ->
            !habit.archived ||
                    habit.id in weeklyByHabit ||
                    habit.id in dailyByHabit
        }

        return visibleHabits.map { habit ->
            FullHabitLogsForWeekMapper.toFullHabitLogsForWeek(
                dailyByHabit[habit.id] ?: emptyList(),
                weeklyByHabit[habit.id],
                habit
            )
        }
    }
}