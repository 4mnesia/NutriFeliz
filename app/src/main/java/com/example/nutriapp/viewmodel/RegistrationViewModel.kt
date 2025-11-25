package com.example.nutriapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriapp.model.User
import com.example.nutriapp.repository.RegistrationResult
import com.example.nutriapp.repository.UserRepository
import com.example.nutriapp.util.PasswordStrength
import com.example.nutriapp.util.calculatePasswordStrength
import com.example.nutriapp.util.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class RegistrationStatus { IDLE, LOADING, SUCCESS, ERROR }

data class RegistrationUiState(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isEmailValid: Boolean = true,
    val passwordsMatch: Boolean = true,
    val passwordStrength: PasswordStrength = PasswordStrength.WEAK,
    val registrationStatus: RegistrationStatus = RegistrationStatus.IDLE,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean
        get() = isEmailValid && passwordsMatch && password.isNotEmpty() && fullName.isNotEmpty() && username.isNotEmpty() && passwordStrength != PasswordStrength.WEAK
}

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onFullNameChange(fullName: String) {
        _uiState.update { it.copy(fullName = fullName, registrationStatus = RegistrationStatus.IDLE) }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, registrationStatus = RegistrationStatus.IDLE) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, isEmailValid = isEmailValid(email), registrationStatus = RegistrationStatus.IDLE) }
    }

    fun onPasswordChange(password: String) {
        val strength = calculatePasswordStrength(password)
        _uiState.update { it.copy(password = password, passwordStrength = strength, passwordsMatch = password == it.confirmPassword, registrationStatus = RegistrationStatus.IDLE) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, passwordsMatch = it.password == confirmPassword, registrationStatus = RegistrationStatus.IDLE) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(registrationStatus = RegistrationStatus.IDLE) }
    }

    fun registerUser() {
        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(registrationStatus = RegistrationStatus.LOADING) }
            val state = _uiState.value
            
            // El constructor de User es correcto, usa fullName y passwordHash implícitamente
            val newUser = User(state.fullName, state.username, state.email, state.password)
            
            // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
            // El `when` ahora maneja todos los casos de la `sealed class` RegistrationResult
            when (val result = userRepository.addUser(newUser)) {
                is RegistrationResult.Success -> {
                    _uiState.update { it.copy(registrationStatus = RegistrationStatus.SUCCESS) }
                }
                is RegistrationResult.Error -> {
                    _uiState.update { it.copy(registrationStatus = RegistrationStatus.ERROR, errorMessage = result.message) }
                }
            }
        }
    }
}
