package com.example.snakegame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.model.Direction
import com.example.snakegame.model.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _state = MutableStateFlow(GameState());
    val state = _state.asStateFlow()

    init {
        startGameLoop();
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (!_state.value.isGameOver) {
                delay(300)
                moveSnake()
            }
        }
    }

    private fun moveSnake() {
        _state.update { currentState ->
            val newBody = currentState.snakeBody.toMutableList()
            val head = newBody.first()

            val newHead = when (currentState.direction) {
                Direction.UP -> head.first to head.second - 1
                Direction.DOWN -> head.first to head.second + 1
                Direction.LEFT -> head.first - 1 to head.second
                Direction.RIGHT -> head.first + 1 to head.second
            }

            val hasHitWall = newHead.first < 0 || newHead.first > 30 || newHead.second < 0 || newHead.second > 30
            if (hasHitWall) {
                return@update currentState.copy(isGameOver = true)
            }
            newBody.add(0, newHead)
            newBody.removeAt(newBody.size - 1)

            currentState.copy(snakeBody = newBody)
        }
    }
        fun onDirectionChange(newDirection: Direction) {
            _state.update { it.copy(direction = newDirection) }
        }

        fun forceGameOver() {
            _state.update {
                it.copy(isGameOver = true)
            }
        }
        fun resetGame() {
            _state.value = GameState()
            startGameLoop()
        }
}