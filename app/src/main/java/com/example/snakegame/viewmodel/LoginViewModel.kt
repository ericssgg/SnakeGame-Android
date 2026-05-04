package com.example.snakegame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.dao.SnakeDAO
import com.example.snakegame.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val message: String = "",
    val errorMsg: String = "",
    val isLoginSuccess: Boolean = false
)

class LoginViewModel(private val snakeDao: SnakeDAO) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val lastUser = snakeDao.getLastLoggedUser()
            if (lastUser != null) {
                _uiState.update { it.copy(username = lastUser.username) }
            }
        }
    }

    fun onUsernameChange(input: String) {
        _uiState.update { it.copy(username = input, errorMsg = "", message = "") }
    }

    fun onPasswordChange(input: String) {
        _uiState.update { it.copy(password = input, errorMsg = "", message = "") }
    }

    fun onLoginClick() {
        val current = _uiState.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(errorMsg = "Por favor, rellena todos los campos") }
            return
        }

        viewModelScope.launch {
            val user = snakeDao.login(current.username, current.password)

            if (user != null) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val currentDate = dateFormat.format(Date())
                snakeDao.insertUser(user.copy(lastLoginDate = currentDate))

                _uiState.update { it.copy(isLoginSuccess = true, errorMsg = "") }
            } else {
                _uiState.update { it.copy(errorMsg = "ERROR: Usuario o contraseña incorrectos") }
            }
        }
    }

    fun onRegisterClick() {
        val current = _uiState.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(errorMsg = "Por favor, rellena todos los campos") }
            return
        }

        viewModelScope.launch {
            val existingUser = snakeDao.getUser(current.username)

            if (existingUser != null) {
                _uiState.update { it.copy(errorMsg = "Este nombre de usuario ya existe") }
            } else {
                // Lo creamos nuevo
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                val newUser = User(
                    username = current.username,
                    password = current.password,
                    lastLoginDate = currentDate
                )
                snakeDao.insertUser(newUser)

                _uiState.update {
                    it.copy(
                        message = "¡Registrado con éxito! Ya puedes darle a Entrar.",
                        errorMsg = ""
                    )
                }
            }
        }
    }

    fun onLogout() {
        _uiState.update {
            it.copy(
                isLoginSuccess = false,
                password = "",
                errorMsg = "",
                message = ""
            )
        }
    }
}