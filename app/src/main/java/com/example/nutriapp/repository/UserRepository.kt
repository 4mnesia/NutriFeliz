package com.example.nutriapp.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.nutriapp.model.User

enum class RegistrationResult {
    SUCCESS,
    USERNAME_EXISTS,
    EMAIL_EXISTS,
    FAILED // Error genérico
}

object UserRepository {
    private val initialUsers = listOf(
        User(
            fullName = "Tester Account",
            username = "tester",
            email = "tester@nutriapp.com",
            passwordHash = "Tester1234"
        )
    )

    private val users = mutableStateListOf<User>(*initialUsers.toTypedArray())

    fun addUser(user: User): RegistrationResult {
        if (users.any { it.username.equals(user.username, ignoreCase = true) }) {
            return RegistrationResult.USERNAME_EXISTS
        }
        if (users.any { it.email.equals(user.email, ignoreCase = true) }) {
            return RegistrationResult.EMAIL_EXISTS
        }
        return if (users.add(user)) {
            RegistrationResult.SUCCESS
        } else {
            RegistrationResult.FAILED
        }
    }

    fun findUser(usernameOrEmail: String, passwordAttempt: String): User? {
        return users.find { (it.username.equals(usernameOrEmail, ignoreCase = true) || it.email.equals(usernameOrEmail, ignoreCase = true)) && it.passwordHash == passwordAttempt }
    }
}
