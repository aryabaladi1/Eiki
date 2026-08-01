package com.habit.habit_tracker.service

import com.habit.habit_tracker.constants.ErrorMessage.USER_IN_USE

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.dao.DataIntegrityViolationException

import com.habit.habit_tracker.domain.User
import com.habit.habit_tracker.dto.request.UserUpdateRequest
import com.habit.habit_tracker.exception.ApiRequestException
import com.habit.habit_tracker.repository.UserRepository
import com.habit.habit_tracker.security.AuthUtil

@Service
class UserService(
    private val userRepository: UserRepository,
    private val authUtil: AuthUtil
) {
    fun getUserDetails(): User {
        return authUtil.getAuthenticatedUser()
    }

    fun updateUserDetails(request: UserUpdateRequest): User {
        val user: User = authUtil.getAuthenticatedUser()

        user.apply {
            request.username?.let { username = it.trim().lowercase() }
            request.firstName?.let { firstName = it }
            request.lastName?.let { lastName = it }
        }

        return try { 
            userRepository.save(user)
        } catch (ex: DataIntegrityViolationException){ // will throw an exception if the username is taken
            throw ApiRequestException(USER_IN_USE, HttpStatus.CONFLICT)
        }
    }
}