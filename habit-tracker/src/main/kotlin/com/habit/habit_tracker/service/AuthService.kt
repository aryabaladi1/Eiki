package com.habit.habit_tracker.service

import com.habit.habit_tracker.constants.ErrorMessage.USER_IN_USE
import com.habit.habit_tracker.constants.ErrorMessage.INVALID_CREDENTIALS

import com.habit.habit_tracker.domain.User
import com.habit.habit_tracker.dto.request.LoginRequest
import com.habit.habit_tracker.dto.request.RegisterRequest
import com.habit.habit_tracker.service.result.AuthResult
import com.habit.habit_tracker.exception.ApiRequestException
import com.habit.habit_tracker.repository.UserRepository
import com.habit.habit_tracker.security.JwtService
import com.habit.habit_tracker.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun registerUser(request: RegisterRequest): AuthResult {
        val username = request.username.trim().lowercase()

        if (userRepository.findByUsername(username).isPresent) {
            throw ApiRequestException(USER_IN_USE, HttpStatus.CONFLICT)
        }

        val newUser = User(
            username = username,
            password = passwordEncoder.encode(request.password)
        )

        val savedUser = userRepository.save(newUser)

        val userPrincipal = UserPrincipal(savedUser)
        val token = jwtService.generateToken(userPrincipal)

        return AuthResult(savedUser, token)
    }

    fun loginUser(request: LoginRequest): AuthResult {
        val username = request.username.trim().lowercase()

        val user = userRepository.findByUsername(username)
            .orElseThrow { ApiRequestException(INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED) }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw ApiRequestException(INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED)
        }

        user.lastLogin = LocalDateTime.now()
        userRepository.save(user)

        val userPrincipal = UserPrincipal(user)
        val token = jwtService.generateToken(userPrincipal)


        return AuthResult(user, token)
    }
}
