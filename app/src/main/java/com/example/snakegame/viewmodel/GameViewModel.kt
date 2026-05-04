package com.example.snakegame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.dao.SnakeDAO
import com.example.snakegame.model.Direction
import com.example.snakegame.model.GameEvent
import com.example.snakegame.model.GameState
import com.example.snakegame.model.GameResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(private val snakeDao: SnakeDAO) : ViewModel() {
    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private var movementJob: Job? = null
    private var timerJob: Job? = null

    init {
        startGameLoop()
    }

    private fun startGameLoop() {
        movementJob?.cancel()
        timerJob?.cancel()

        movementJob = viewModelScope.launch {
            while (!_state.value.isGameOver) {
                delay(300)
                if(!_state.value.isStopped){
                    moveSnake()
                }
            }
        }
        timerJob = viewModelScope.launch {
            while (!_state.value.isGameOver) {
                delay(1000)
                if (!_state.value.isStopped) {
                    _state.update { it.copy(timeElapsed = it.timeElapsed + 1) }
                }
            }
        }
    }

    private fun moveSnake() {
        var diedThisTurn = false

        _state.update { currentState ->
            val newBody = currentState.snakeBody.toMutableList()
            val head = newBody.first()

            val newHead = when (currentState.direction) {
                Direction.UP -> head.first to head.second - 1
                Direction.DOWN -> head.first to head.second + 1
                Direction.LEFT -> head.first - 1 to head.second
                Direction.RIGHT -> head.first + 1 to head.second
            }

            val hasHitWall = newHead.first < 0 || newHead.first > 19 || newHead.second < 0 || newHead.second > 19
            val hasHitSelf = currentState.snakeBody.contains(newHead)

            if (hasHitWall || hasHitSelf) {
                diedThisTurn = true // Marcamos que ha muerto
                return@update currentState.copy(isGameOver = true)
            }

            newBody.add(0, newHead)
            if (newHead == currentState.food) {
                var newFood: Pair<Int, Int>
                do {
                    newFood = Pair((0..19).random(), (0..19).random())
                } while (newBody.contains(newFood))

                currentState.copy(snakeBody = newBody.toList(), food = newFood)
            } else {
                newBody.removeAt(newBody.size - 1)
                currentState.copy(snakeBody = newBody.toList())
            }
        }

        if (diedThisTurn) {
            saveMatchResult()
        }
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.Move -> {
                _state.update { currentState ->
                    val currentDirection = currentState.direction
                    val isOpposite = when (event.direction) {
                        Direction.UP -> currentDirection == Direction.DOWN
                        Direction.DOWN -> currentDirection == Direction.UP
                        Direction.LEFT -> currentDirection == Direction.RIGHT
                        Direction.RIGHT -> currentDirection == Direction.LEFT
                    }
                    if (isOpposite) currentState else currentState.copy(direction = event.direction)
                }
            }
            GameEvent.ForceGameOver -> {
                _state.update { it.copy(isGameOver = true) }
                saveMatchResult()
            }
            GameEvent.ResetGame -> {
                _state.value = GameState()
                startGameLoop()
            }
            GameEvent.Pause -> {
                _state.update { it.copy(isStopped = true) }
            }
            GameEvent.Resume -> {
                _state.update { it.copy(isStopped =  false) }
            }
        }
    }

    private fun saveMatchResult() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {

            val currentUser = snakeDao.getLastLoggedUser()
            val realPlayerName = currentUser?.username ?: "Anónimo"

            val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
            val currentDate = dateFormat.format(java.util.Date())

            val result = com.example.snakegame.model.GameResult(
                playerName = realPlayerName,
                score = _state.value.snakeBody.size,
                date = currentDate
            )
            snakeDao.insertGameResult(result)
        }
    }
    fun setSnakeColor(color: androidx.compose.ui.graphics.Color) {
        _state.update { it.copy(snakeColor = color) }
    }
}