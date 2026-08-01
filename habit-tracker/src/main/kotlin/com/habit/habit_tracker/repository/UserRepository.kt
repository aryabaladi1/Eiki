package com.habit.habit_tracker.repository

import com.habit.habit_tracker.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {

    // @Lock(LockModeType.PESSIMISTIC_WRITE) --- Imma remove this later
    fun findByUsername(username: String): Optional<User>
}
