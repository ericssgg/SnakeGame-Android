package com.example.snakegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snakegame.dao.SnakeDatabase
import com.example.snakegame.ui.theme.SnakeGameTheme
import com.example.snakegame.viewmodel.GameViewModel
import com.example.snakegame.viewmodel.LoginViewModel
import com.example.snakegame.controller.NavGraph
import com.example.snakegame.viewmodel.NetworkViewModel
import com.example.snakegame.viewmodel.StatsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SnakeGameTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val db = remember { SnakeDatabase.getDatabase(context) }

                    val gameViewModel: GameViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return GameViewModel(db.snakeDao) as T
                            }
                        }
                    )

                    val loginViewModel: LoginViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return LoginViewModel(db.snakeDao) as T
                            }
                        }
                    )
                    val statsViewModel: StatsViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return StatsViewModel(db.snakeDao) as T
                            }
                        }
                    )
                    val networkViewModel: NetworkViewModel = viewModel()
                    NavGraph(
                        gameViewModel = gameViewModel,
                        loginViewModel = loginViewModel,
                        statsViewModel = statsViewModel,
                        networkViewModel = networkViewModel
                    )
                }
            }
        }
    }
}