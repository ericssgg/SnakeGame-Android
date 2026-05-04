package com.example.snakegame.model
import com.example.snakegame.model.Direction
sealed interface GameEvent {
    data class Move(val direction: Direction) : GameEvent
    data object ForceGameOver : GameEvent
    data object ResetGame : GameEvent
    data object Pause : GameEvent
    data object Resume : GameEvent
}