package com.example.nutriapp.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.nutriapp.model.User

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

    fun addUser(user: User): Boolean {
        return if (users.none { it.username.equals(user.username, ignoreCase = true) || it.email.equals(user.email, ignoreCase = true) }) {
            users.add(user)
            true
        } else {
            false
        }
    }

    fun findUser(usernameOrEmail: String, passwordAttempt: String): User? {
        return users.find { (it.username.equals(usernameOrEmail, ignoreCase = true) || it.email.equals(usernameOrEmail, ignoreCase = true)) && it.passwordHash == passwordAttempt }
    }
}
