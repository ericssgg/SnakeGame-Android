package com.example.snakegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.snakegame.ui.theme.SnakeGameTheme
import com.example.snakegame.viewmodel.GameViewModel
import com.example.snakegame.controller.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameViewModel = GameViewModel()
        setContent {
            SnakeGameTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ){
                    NavGraph(gameViewModel = gameViewModel)
                }
            }
        }
    }
}