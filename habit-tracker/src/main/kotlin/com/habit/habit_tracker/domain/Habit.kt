package com.habit.habit_tracker.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

import java.time.LocalDateTime

@Entity
@Table(name = "habits")
data class Habit(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habits_seq")
    @SequenceGenerator(name = "habits_seq", sequenceName = "habits_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
    val user: User,

    @Column(name = "name", nullable = false)
    @field:Size(max = 50)
    var name: String,

    @Column(name = "description")
    @field:Size(max = 200)
    var description: String? = null,

    @Column(name = "minutes_total", nullable = false)
    @field:Min(0)
    var minutesTotal: Int = 0,

    @Column(name = "archived", nullable = false)
    var archived: Boolean = false,

    @Column(name = "archived_at")
    var archivedAt: LocalDateTime? = null,

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