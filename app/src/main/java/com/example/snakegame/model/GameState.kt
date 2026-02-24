package com.example.snakegame.model

data class GameState(
    // Lista de pares (X, Y). El primer elemento es la cabeza.
    // La serpiente empieza con 3 cuadraditos en una posición fija.
    val snakeBody: List<Pair<Int, Int>> = listOf(10 to 10, 10 to 11, 10 to 12),
    val food: Pair<Int, Int> = 5 to 5, // Posición de la comida. Aparece siempre en el mismo sitio de momento.

    val direction: Direction = Direction.RIGHT,

    val isGameOver: Boolean = false
)
