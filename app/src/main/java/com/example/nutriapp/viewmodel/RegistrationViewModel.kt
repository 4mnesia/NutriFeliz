package com.example.nutriapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriapp.model.User
import com.example.nutriapp.repository.RegistrationResult
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
    val registrationResult: RegistrationResult? = null
) {
    val isFormValid: Boolean
        get() = isEmailValid && passwordsMatch && password.isNotEmpty() && fullName.isNotEmpty() && username.isNotEmpty() && passwordStrength != PasswordStrength.WEAK
}

class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onFullNameChange(fullName: String) {
        _uiState.update { it.copy(fullName = fullName, registrationResult = null) }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, registrationResult = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, isEmailValid = isEmailValid(email), registrationResult = null) }
    }

    fun onPasswordChange(password: String) {
        val strength = calculatePasswordStrength(password)
        _uiState.update { it.copy(password = password, passwordStrength = strength, passwordsMatch = password == it.confirmPassword, registrationResult = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, passwordsMatch = it.password == confirmPassword, registrationResult = null) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(registrationResult = null) }
    }

    fun registerUser() {
        viewModelScope.launch {
            val state = _uiState.value
            val newUser = User(state.fullName, state.username, state.email, state.password)
            val result = UserRepository.addUser(newUser)
            _uiState.update { it.copy(registrationResult = result) }
        }
    }
}
