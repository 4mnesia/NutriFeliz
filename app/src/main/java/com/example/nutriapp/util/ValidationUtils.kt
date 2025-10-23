package com.example.nutriapp.util

import android.util.Patterns

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

enum class PasswordStrength {
    WEAK, MEDIUM, STRONG, VERY_STRONG
}

fun calculatePasswordStrength(password: String): PasswordStrength {
    val hasLowercase = password.any { it.isLowerCase() }
    val hasUppercase = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }

    val score = listOf(hasLowercase, hasUppercase, hasDigit, hasSpecialChar, password.length >= 8).count { it }

    return when (score) {
        0, 1, 2 -> PasswordStrength.WEAK
        3 -> PasswordStrength.MEDIUM
        4 -> PasswordStrength.STRONG
        5 -> PasswordStrength.VERY_STRONG
        else -> PasswordStrength.WEAK
    }
}
