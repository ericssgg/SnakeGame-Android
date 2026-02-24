package com.example.snakegame.viewmodel

import androidx.lifecycle.ViewModel
import com.example.snakegame.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val message: String = "",
    val errorMsg: String = "",
    val isLoginSuccess: Boolean = false // Lo uso para saber cuándo navegar al juego
)

class LoginViewModel: ViewModel() {

    private val ADMIN_USER = "admin"
    private val ADMIN_PASS = "1234"

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onUsernameChange(input: String) {
        _uiState.update { it.copy(username = input, errorMsg = "", message = "") }
    }

    fun onPasswordChange(input: String) {
        _uiState.update { it.copy(password = input, errorMsg = "", message = "") }
    }

    fun onLoginClick() {
        val current = _uiState.value
        if (current.username == ADMIN_USER && current.password == ADMIN_PASS) {
            _uiState.update { it.copy(isLoginSuccess = true, errorMsg = "") }
        } else {
            _uiState.update { it.copy(errorMsg = "ERROR: Credenciales incorrectas") }
        }
    }
}