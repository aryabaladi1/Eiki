package com.habit.habit_tracker.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

import java.time.LocalDateTime
import java.time.LocalDate

@Entity
@Table(
    name = "daily_habit_logs",
    uniqueConstraints = [UniqueConstraint(columnNames = ["habit_id", "date"])]
)
data class DailyHabitLog(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dhl_seq")
    @SequenceGenerator(name = "dhl_seq", sequenceName = "dhl_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", referencedColumnName = "id", nullable = false)
    val habit: Habit,

    @Column(name = "minutes_done", nullable = false)
    @field:Min(0)
    @field:Max(1440)
    var minutesDone: Int = 0,

    @Column(name = "date", nullable = false)
    val date: LocalDate,

    @Column(name = "created_at", updatable = false, nullable = false)
    var createdAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now()
        }
    }
}