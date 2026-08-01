package com.habit.habit_tracker.repository

import com.habit.habit_tracker.domain.Habit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface HabitRepository : JpaRepository<Habit, Long> {

    @Query("SELECT h FROM Habit h WHERE h.id = :habitId AND h.user.id = :userId")
    fun findByIdAndUserId(@Param("habitId") habitId: Long, @Param("userId") userId: Long): Optional<Habit>

    @Query("SELECT h FROM Habit h WHERE h.user.id = :userId ORDER BY h.archived ASC, h.createdAt ASC")
    fun findAllByUserId(@Param("userId") userId: Long): List<Habit>

    fun countByUserId(userId: Long): Long
}