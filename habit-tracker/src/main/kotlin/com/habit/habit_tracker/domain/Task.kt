package com.habit.habit_tracker.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Size

import java.time.LocalDateTime
import com.habit.habit_tracker.enums.TaskStatus
import com.habit.habit_tracker.enums.TaskDifficulty
import java.time.LocalDate


@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_seq")
    @SequenceGenerator(name = "tasks_seq", sequenceName = "tasks_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    val user: User,

    @Column(name = "name", nullable = false)
    @field:Size(max = 50)
    var name: String,

    @Column(name = "description")
    @field:Size(max = 200)
    var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: TaskStatus = TaskStatus.PENDING,

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    var difficulty: TaskDifficulty? = null,

    @Column(name = "due_date")
    val dueDate: LocalDate? = null,

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