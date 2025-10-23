package com.example.nutriapp.data

import androidx.compose.runtime.mutableStateListOf

/**
 * Modelo de datos para un usuario.
 */
data class User(
    val fullName: String,
    val username: String,
    val email: String,
    val passwordHash: String // En un futuro, aquí guardaríamos el hash de la contraseña, no el texto plano
)

/**
 * Una "base de datos" temporal en memoria para almacenar usuarios.
 * Incluye un usuario de prueba por defecto.
 */
object UserRepository {
    private val initialUsers = listOf(
        User(
            fullName = "Tester Account",
            username = "tester",
            email = "tester@nutriapp.com",
            passwordHash = "Tester1234" // Contraseña en texto plano solo para esta simulación
        )
    )

    val users = mutableStateListOf<User>(*initialUsers.toTypedArray())

    fun addUser(user: User) {
        // Evitar duplicados por nombre de usuario o email
        if (users.none { it.username == user.username || it.email == user.email }) {
            users.add(user)
        }
    }

    fun findUser(usernameOrEmail: String, passwordAttempt: String): User? {
        return users.find { (it.username == usernameOrEmail || it.email == usernameOrEmail) && it.passwordHash == passwordAttempt }
    }
}
