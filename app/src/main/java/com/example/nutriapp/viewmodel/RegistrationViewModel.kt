package com.example.nutriapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriapp.model.User
import com.example.nutriapp.repository.UserRepository
import com.example.nutriapp.util.PasswordStrength
import com.example.nutriapp.util.calculatePasswordStrength
import com.example.nutriapp.util.isEmailValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isEmailValid: Boolean = true,
    val passwordsMatch: Boolean = true,
    val passwordStrength: PasswordStrength = PasswordStrength.WEAK,
    val registrationSuccess: Boolean = false
) {
    val isFormValid: Boolean
        get() = isEmailValid && passwordsMatch && password.isNotEmpty() && fullName.isNotEmpty() && username.isNotEmpty() && passwordStrength != PasswordStrength.WEAK
}

class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onFullNameChange(fullName: String) {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, isEmailValid = isEmailValid(email)) }
    }

    fun onPasswordChange(password: String) {
        val strength = calculatePasswordStrength(password)
        _uiState.update { it.copy(password = password, passwordStrength = strength, passwordsMatch = password == it.confirmPassword) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, passwordsMatch = it.password == confirmPassword) }
    }

    fun registerUser() {
        viewModelScope.launch {
            val state = _uiState.value
            val newUser = User(state.fullName, state.username, state.email, state.password) // Idealmente, hashear la contraseña
            val success = UserRepository.addUser(newUser)
            _uiState.update { it.copy(registrationSuccess = success) }
        }
    }
}
