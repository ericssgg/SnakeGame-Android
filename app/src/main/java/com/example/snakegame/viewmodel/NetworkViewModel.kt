package com.example.snakegame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NetworkViewModel : ViewModel() {

    private val _serverMessage = MutableStateFlow("Conectando con el servidor...")
    val serverMessage = _serverMessage.asStateFlow()

    init {
        fetchMessage()
    }

    private fun fetchMessage() {
        viewModelScope.launch {
            try {

                val response = RetrofitClient.api.getServerMessage()

                _serverMessage.value = "Online: Servidor conectado correctamente"
            } catch (e: Exception) {
                _serverMessage.value = " Sin conexión: Jugando en Modo Local"
            }
        }
    }
}