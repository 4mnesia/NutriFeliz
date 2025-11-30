package com.example.nutriapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriapp.data.SessionManager
import com.example.nutriapp.network.UsuarioDTO // Importamos el DTO de red
import com.example.nutriapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LoginStatus { IDLE, SUCCESS, ERROR, LOADING }

// --- ¡CORRECCIÓN 1! ---
// Cambiamos el tipo de `loggedInUser` para que coincida con la respuesta del backend.
data class LoginUiState(
    val usernameOrEmail: String = "",
    val password: String = "",
    val loginStatus: LoginStatus = LoginStatus.IDLE,
    val loggedInUser: UsuarioDTO? = null // Antes era User?
)

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameOrEmailChange(value: String) {
        _uiState.update { it.copy(usernameOrEmail = value, loginStatus = LoginStatus.IDLE) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, loginStatus = LoginStatus.IDLE) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(loginStatus = LoginStatus.IDLE) }
    }

    // --- ¡CORRECCIÓN 2! ---
    // La lógica ahora maneja el objeto UsuarioDTO que viene del repositorio.
    fun login() {
        viewModelScope.launch {

            _uiState.update { it.copy(loginStatus = LoginStatus.LOADING) }
            val state = _uiState.value
            try {
                // userRepository.findUser ahora devuelve un UsuarioDTO?
                val userDto = userRepository.findUser(state.usernameOrEmail, state.password)
                if (userDto != null) {
                    sessionManager.saveUserId(userDto.id)
                    _uiState.update { it.copy(loginStatus = LoginStatus.SUCCESS, loggedInUser = userDto) }
                } else {
                    _uiState.update { it.copy(loginStatus = LoginStatus.ERROR) }
                }
            } catch (e: Exception) {
                // Capturamos cualquier error de red o de otro tipo
                _uiState.update { it.copy(loginStatus = LoginStatus.ERROR) }
            }
        }
    }
}
