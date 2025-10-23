package com.example.nutriapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriapp.model.User
import com.example.nutriapp.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LoginStatus { IDLE, SUCCESS, ERROR, LOADING }

data class LoginUiState(
    val usernameOrEmail: String = "",
    val password: String = "",
    val loginStatus: LoginStatus = LoginStatus.IDLE,
    val loggedInUser: User? = null
)

class LoginViewModel : ViewModel() {

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

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(loginStatus = LoginStatus.LOADING) }
            val state = _uiState.value
            val user = UserRepository.findUser(state.usernameOrEmail, state.password)
            if (user != null) {
                delay(500)
                _uiState.update { it.copy(loginStatus = LoginStatus.SUCCESS, loggedInUser = user) }
            } else {
                _uiState.update { it.copy(loginStatus = LoginStatus.ERROR) }
            }
        }
    }
}
