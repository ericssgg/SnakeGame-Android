package com.example.snakegame.dao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.snakegame.model.GameResult
import com.example.snakegame.model.User

@Database(entities = [User::class, GameResult::class], version = 1, exportSchema = false)
abstract class SnakeDatabase : RoomDatabase() {

    abstract val snakeDao: SnakeDAO

    companion object {
        @Volatile
        private var INSTANCE: SnakeDatabase? = null

        fun getDatabase(context: Context): SnakeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SnakeDatabase::class.java,
                    "snake_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}