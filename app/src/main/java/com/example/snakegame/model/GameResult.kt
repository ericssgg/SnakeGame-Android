package com.example.snakegame.model

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "game_history")
    data class GameResult(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val playerName: String,
        val score: Int,
        val date: String
    )