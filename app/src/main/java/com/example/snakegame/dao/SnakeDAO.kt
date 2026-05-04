package com.example.snakegame.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.snakegame.model.GameResult
import com.example.snakegame.model.User
import kotlinx.coroutines.flow.Flow
@Dao
interface SnakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users ORDER BY lastLoginDate DESC LIMIT 1")
    suspend fun getLastLoggedUser(): User?

    @Insert
    suspend fun insertGameResult(result: GameResult)

    @Query("SELECT * FROM game_history ORDER BY score DESC LIMIT 10")
    fun getTopScores(): Flow<List<GameResult>>

    @Query("SELECT * FROM game_history WHERE playerName = :name ORDER BY score DESC")
    fun getScoresForPlayer(name: String): Flow<List<GameResult>>

    @Query("SELECT * FROM users WHERE username = :user AND password = :pass")
    suspend fun login(user: String, pass: String): User?

    @Query("SELECT * FROM users WHERE username = :user")
    suspend fun getUser(user: String): User?
}
