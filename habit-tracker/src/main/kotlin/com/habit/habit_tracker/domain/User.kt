package com.habit.habit_tracker.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Size

import java.time.LocalDateTime

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["username"])]
    )
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "username", nullable = false, unique = true, length = 50)
    @field:Size(min = 3, max = 50)
    var username: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "first_name")
    @field:Size(max = 50)
    var firstName: String? = null,

    @Column(name = "last_name")
    @field:Size(max = 50)
    var lastName: String? = null,

    @Column(name = "last_login")
    var lastLogin: LocalDateTime? = null,

    @Column(name = "created_at", updatable = false, nullable = false)
    var createdAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now()
            lastLogin = createdAt
        }
    }
}