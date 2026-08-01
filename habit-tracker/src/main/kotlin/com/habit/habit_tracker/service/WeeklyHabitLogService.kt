package com.habit.habit_tracker.service

import com.habit.habit_tracker.constants.ErrorMessage.HABIT_NOT_FOUND
import com.habit.habit_tracker.constants.ErrorMessage.HABIT_ARCHIVED

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

import com.habit.habit_tracker.domain.WeeklyHabitLog
import com.habit.habit_tracker.dto.request.UpdateWeeklyGoalRequest
import com.habit.habit_tracker.exception.ApiRequestException
import com.habit.habit_tracker.repository.HabitRepository
import com.habit.habit_tracker.repository.WeeklyHabitLogRepository
import com.habit.habit_tracker.security.AuthUtil
import org.springframework.transaction.annotation.Transactional

@Service
class WeeklyHabitLogService(
    private val weeklyHabitLogRepository: WeeklyHabitLogRepository,
    private val habitRepository: HabitRepository,
    private val authUtil: AuthUtil
) {

    @Transactional
    fun updateWeeklyGoal(
        habitId: Long,
        request: UpdateWeeklyGoalRequest
    ): WeeklyHabitLog {

        if (request.weekEnd.isBefore(request.weekStart)) {
            throw ApiRequestException(
                "Week end cannot be before week start",
                HttpStatus.BAD_REQUEST
            )
        }

        val user = authUtil.getAuthenticatedUser()

        val habit = habitRepository.findByIdAndUserId(
            habitId,
            user.id!!
        )
            .orElseThrow {
                ApiRequestException(
                    HABIT_NOT_FOUND,
                    HttpStatus.NOT_FOUND
                )
            }

        if (habit.archived) {
            throw ApiRequestException(
                HABIT_ARCHIVED,
                HttpStatus.FORBIDDEN
            )
        }

        val weeklyLog =
            weeklyHabitLogRepository.findByHabitIdAndWeekRange(
                habitId,
                request.weekStart,
                request.weekEnd
            )
                .orElseGet {
                    WeeklyHabitLog(
                        habit = habit,
                        weekStart = request.weekStart,
                        weekEnd = request.weekEnd
                    )
                }

        weeklyLog.weeklyGoal = request.weeklyGoal

        return weeklyHabitLogRepository.save(weeklyLog)
    }
}
