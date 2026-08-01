package com.habit.habit_tracker.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import com.habit.habit_tracker.dto.request.TaskCreateRequest
import com.habit.habit_tracker.dto.response.TaskResponse
import com.habit.habit_tracker.dto.request.TaskUpdateRequest
import com.habit.habit_tracker.service.TaskService
import com.habit.habit_tracker.mapper.TaskMapper

import jakarta.validation.Valid

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(private val taskService: TaskService) {
    @PostMapping
    fun createTask(@Valid @RequestBody request: TaskCreateRequest): ResponseEntity<TaskResponse> {
        val task = taskService.createTask(request)
        val response = TaskMapper.toTaskResponse(task)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{taskId}")
    fun updateTask(
        @PathVariable taskId: Long, 
        @Valid @RequestBody request: TaskUpdateRequest
    ): ResponseEntity<TaskResponse> {
        val task = taskService.updateTask(taskId, request)
        val response = TaskMapper.toTaskResponse(task)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{taskId}")
    fun getTask(@PathVariable taskId: Long): ResponseEntity<TaskResponse> {
        val task = taskService.getTask(taskId)
        val response = TaskMapper.toTaskResponse(task)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAllTasks(): ResponseEntity<List<TaskResponse>> {
        val tasks = taskService.getAllTasks()
        val response = tasks.map { TaskMapper.toTaskResponse(it) }
        return ResponseEntity.ok(response)
    }
}