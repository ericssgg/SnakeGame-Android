package com.example.snakegame.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    var password: String,
    val lastLoginDate: String
)
