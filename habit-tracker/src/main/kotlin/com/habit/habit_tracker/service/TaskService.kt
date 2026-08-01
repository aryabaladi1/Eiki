package com.habit.habit_tracker.service

import com.habit.habit_tracker.constants.ErrorMessage.USER_NOT_FOUND
import com.habit.habit_tracker.constants.ErrorMessage.TASK_NOT_FOUND

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.habit.habit_tracker.domain.Task
import com.habit.habit_tracker.dto.request.TaskCreateRequest
import com.habit.habit_tracker.dto.request.TaskUpdateRequest
import com.habit.habit_tracker.exception.ApiRequestException
import com.habit.habit_tracker.repository.TaskRepository
import com.habit.habit_tracker.repository.UserRepository
import com.habit.habit_tracker.security.AuthUtil

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val authUtil: AuthUtil
) {
    fun createTask(request: TaskCreateRequest): Task {
        val user = authUtil.getAuthenticatedUser()
            .let { userRepository.findById(it.id!!).orElseThrow {
                ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND)
            } } // This is necessary for now, due to detached entity issues :<

        return taskRepository.save(
            Task(
                name = request.name.trim(),
                description = request.description,
                user = user,
                dueDate = request.dueDate,
                difficulty = request.difficulty
            )
        )
    }

    @Transactional
    fun updateTask(taskId: Long, request: TaskUpdateRequest): Task {
        val user = authUtil.getAuthenticatedUser()
        val task = taskRepository.findByIdAndUserId(taskId, user.id!!)
            .orElseThrow { ApiRequestException(TASK_NOT_FOUND, HttpStatus.NOT_FOUND) }

        task.apply {
            request.name?.let { name = it.trim() }
            request.description?.let { description = it.trim() }
            request.difficulty?.let { difficulty = it }
            request.status?.let { status = it }
        }

        return taskRepository.save(task)
    }

    fun getTask(taskId: Long): Task {
        val user = authUtil.getAuthenticatedUser()
        return taskRepository.findByIdAndUserId(taskId, user.id!!)
            .orElseThrow { ApiRequestException(TASK_NOT_FOUND, HttpStatus.NOT_FOUND)}
    }

    fun getAllTasks(): List<Task> {
        val user = authUtil.getAuthenticatedUser()
        return taskRepository.findAllByUserId(user.id!!)
    }
}