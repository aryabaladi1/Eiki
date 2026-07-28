package com.habit.habit_tracker.service


import com.habit.habit_tracker.constants.ErrorMessage.USER_NOT_FOUND
import com.habit.habit_tracker.constants.ErrorMessage.HABIT_NOT_FOUND
import com.habit.habit_tracker.constants.ErrorMessage.HABIT_ARCHIVED
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

import com.habit.habit_tracker.domain.Habit
import com.habit.habit_tracker.dto.request.HabitCreateRequest
import com.habit.habit_tracker.dto.request.HabitUpdateRequest
import com.habit.habit_tracker.exception.ApiRequestException
import com.habit.habit_tracker.repository.HabitRepository
import com.habit.habit_tracker.repository.UserRepository
import com.habit.habit_tracker.security.AuthUtil
import java.time.LocalDateTime


@Service
class HabitService(
    private val habitRepository: HabitRepository,
    private val userRepository: UserRepository,
    private val authUtil: AuthUtil
) {
    fun createHabit(request: HabitCreateRequest): Habit {
        val user = authUtil.getAuthenticatedUser()
            .let { userRepository.findById(it.id!!).orElseThrow {
                ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND)
            } } // This is necessary for now, due to detached entity issues :<

        return habitRepository.save(
            Habit(
                user = user,
                name = request.name,
                description = request.description
            )
        )
    }

    fun updateHabit(habitId: Long, request: HabitUpdateRequest): Habit {
        val user = authUtil.getAuthenticatedUser()
        val habit = habitRepository.findByIdAndUserId(habitId, user.id!!)
            .orElseThrow { ApiRequestException(HABIT_NOT_FOUND, HttpStatus.NOT_FOUND)}

        if (habit.archived) {
            throw ApiRequestException(
                HABIT_ARCHIVED,
                HttpStatus.FORBIDDEN
            )
        }

        var updated = false

        habit.apply {
            request.name?.takeIf { it.isNotBlank() }?.let { 
                name = it
                updated = true
            }
            request.description?.takeIf {it.isNotBlank()}?.let { 
                description = it 
                updated = true
            }
        }

        return if (updated) habitRepository.save(habit) else habit
    }

    fun archiveHabit(habitId: Long) : Habit {
        val user = authUtil.getAuthenticatedUser()
        val habit = habitRepository.findByIdAndUserId(habitId, user.id!!)
            .orElseThrow { ApiRequestException(HABIT_NOT_FOUND, HttpStatus.NOT_FOUND)}

        if (!habit.archived) {
            habit.archived = true
            habit.archivedAt = LocalDateTime.now()
        }

        return habitRepository.save(habit)
    }

    fun unarchiveHabit(habitId: Long) : Habit {
        val user = authUtil.getAuthenticatedUser()
        val habit = habitRepository.findByIdAndUserId(habitId, user.id!!)
            .orElseThrow { ApiRequestException(HABIT_NOT_FOUND, HttpStatus.NOT_FOUND)}

        if (habit.archived) {
            habit.archived = false
            habit.archivedAt = null
        }

        return habitRepository.save(habit)
    }

    fun getHabit(habitId    : Long): Habit {
        val user = authUtil.getAuthenticatedUser()
        return habitRepository.findByIdAndUserId(habitId, user.id!!)
            .orElseThrow { ApiRequestException(HABIT_NOT_FOUND, HttpStatus.NOT_FOUND)}
    }

    fun getAllHabits(): List<Habit> {
        val user = authUtil.getAuthenticatedUser()
        return habitRepository.findAllByUserId(user.id!!)
    }
}