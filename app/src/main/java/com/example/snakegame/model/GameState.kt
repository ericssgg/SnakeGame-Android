package com.example.snakegame.model

import androidx.compose.ui.graphics.Color

data class GameState(
    val snakeBody: List<Pair<Int, Int>> = listOf(10 to 10, 10 to 11, 10 to 12),
    val food: Pair<Int, Int> = 5 to 5,

    val direction: Direction = Direction.RIGHT,

    val isGameOver: Boolean = false,

    val isStopped: Boolean = false,

    val timeElapsed: Int = 0,

    val snakeColor: Color = Color(0xFF4CAF50)
)
