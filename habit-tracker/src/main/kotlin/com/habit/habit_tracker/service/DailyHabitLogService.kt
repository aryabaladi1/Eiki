package com.habit.habit_tracker.service

import java.time.LocalDate

import com.habit.habit_tracker.constants.ErrorMessage.HABIT_NOT_FOUND
import com.habit.habit_tracker.constants.ErrorMessage.DHL_NOT_FOUND
import com.habit.habit_tracker.constants.ErrorMessage.HABIT_ARCHIVED

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.habit.habit_tracker.domain.DailyHabitLog
import com.habit.habit_tracker.dto.request.DailyHabitLogRequest
import com.habit.habit_tracker.exception.ApiRequestException
import com.habit.habit_tracker.repository.DailyHabitLogRepository
import com.habit.habit_tracker.repository.HabitRepository
import com.habit.habit_tracker.security.AuthUtil

@Service
class DailyHabitLogService(
    private val dailyHabitLogRepository: DailyHabitLogRepository,
    private val habitRepository: HabitRepository,
    private val authUtil: AuthUtil
) {
    @Transactional
    fun saveDailyHabitLog(habitId: Long, request: DailyHabitLogRequest): DailyHabitLog {
        if (request.date.isAfter(LocalDate.now())) {
            throw ApiRequestException(
                "Cannot create logs for future dates",
                HttpStatus.BAD_REQUEST
            )
        }

        val user = authUtil.getAuthenticatedUser()

        val habit = habitRepository.findByIdAndUserId(habitId, user.id!!)
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

        val existingLog = dailyHabitLogRepository
            .findByHabitAndDate(habitId, request.date)
            .orElse(null)

        val dhl = existingLog?.apply {
            request.minutesDone?.let { newMinutes ->
                val minutesDoneChange = newMinutes - minutesDone

                if (habit.minutesTotal + minutesDoneChange < 0) {
                    throw ApiRequestException(
                        "Habit minutes total cannot be negative",
                        HttpStatus.BAD_REQUEST
                    )
                }

                minutesDone = newMinutes
                habit.minutesTotal += minutesDoneChange

                habitRepository.save(habit)
            }
        } ?: DailyHabitLog(
            habit = habit,
            date = request.date,
            minutesDone = request.minutesDone ?: 0
        ).also {
            habit.minutesTotal += it.minutesDone
            habitRepository.save(habit)
        }

        return dailyHabitLogRepository.save(dhl)
    }

    fun getDailyHabitLog(habitId: Long, date: LocalDate): DailyHabitLog {
        val user = authUtil.getAuthenticatedUser()

        habitRepository.findByIdAndUserId(habitId, user.id!!)
            .orElseThrow {
                ApiRequestException(
                    HABIT_NOT_FOUND,
                    HttpStatus.NOT_FOUND
                )
            }

        return dailyHabitLogRepository.findByHabitAndDate(habitId, date)
            .orElseThrow {
                ApiRequestException(
                    DHL_NOT_FOUND,
                    HttpStatus.NOT_FOUND
                )
            }
    }
}