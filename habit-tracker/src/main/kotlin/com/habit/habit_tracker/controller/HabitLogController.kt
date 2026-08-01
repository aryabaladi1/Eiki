package com.habit.habit_tracker.controller

import com.habit.habit_tracker.dto.request.DailyHabitLogRequest
import com.habit.habit_tracker.dto.response.DailyHabitLogResponse
import com.habit.habit_tracker.dto.request.UpdateWeeklyGoalRequest
import com.habit.habit_tracker.dto.response.FullHabitLogsForWeekResponse
import com.habit.habit_tracker.dto.response.UpdateWeeklyGoalResponse
import com.habit.habit_tracker.mapper.DailyHabitLogMapper
import com.habit.habit_tracker.exception.ApiRequestException
import com.habit.habit_tracker.mapper.WeeklyHabitLogMapper
import com.habit.habit_tracker.service.DailyHabitLogService
import com.habit.habit_tracker.service.FullHabitLogService
import com.habit.habit_tracker.service.WeeklyHabitLogService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("/api/v1/logs")
class HabitLogController(
    private val dailyHabitLogService: DailyHabitLogService,
    private val weeklyHabitLogService: WeeklyHabitLogService,
    private val fullHabitLogService: FullHabitLogService
) {

    @PutMapping("/daily/{habitId}")
    fun saveDailyHabitLog(
        @PathVariable habitId: Long, 
        @Valid @RequestBody request: DailyHabitLogRequest
        ): ResponseEntity<DailyHabitLogResponse> {
        val dailyHabitLog = dailyHabitLogService.saveDailyHabitLog(habitId, request)
        val response = DailyHabitLogMapper.toDailyHabitLogResponse(dailyHabitLog)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/weekly/{habitId}/goal")
    fun updateWeeklyGoal(
        @PathVariable habitId: Long, 
        @Valid @RequestBody request: UpdateWeeklyGoalRequest
        ): ResponseEntity<UpdateWeeklyGoalResponse> {
        val weeklyHabitLog = weeklyHabitLogService.updateWeeklyGoal(habitId, request)
        val response =
            WeeklyHabitLogMapper.toUpdateWeeklyGoalResponse(
                weeklyHabitLog
            )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/daily")
    fun getDailyHabitLog(
        @RequestParam("habitId") habitId: Long,
        @RequestParam("date") dateString: String
    ): ResponseEntity<DailyHabitLogResponse> {
        return try {
            val date = LocalDate.parse(dateString)
            val dailyHabitLog = dailyHabitLogService.getDailyHabitLog(habitId, date)
            val response = DailyHabitLogMapper.toDailyHabitLogResponse(dailyHabitLog)
            ResponseEntity.ok(response)
        } catch (e: DateTimeParseException) {
            throw ApiRequestException("Invalid date format. Please use YYYY-MM-DD.", HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/week")
    fun getFullHabitLogsForWeek(
        @RequestParam("weekStart") weekStartString: String,
        @RequestParam("weekEnd") weekEndString: String
    ): ResponseEntity<List<FullHabitLogsForWeekResponse>> {
        return try {
            val weekStart = LocalDate.parse(weekStartString)
            val weekEnd = LocalDate.parse(weekEndString)

            val fullHabitLogsForWeek = fullHabitLogService.getFullHabitLogsForWeek(weekStart, weekEnd);

            ResponseEntity.ok(fullHabitLogsForWeek)

        } catch (e: DateTimeParseException) {
            throw ApiRequestException("Invalid date format. Please use YYYY-MM-DD.", HttpStatus.BAD_REQUEST)
        }
    }
}
