package com.example.snakegame.controller

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snakegame.model.GameEvent
import com.example.snakegame.viewmodel.GameViewModel
import com.example.snakegame.ui.view.login.LoginScreen
import com.example.snakegame.ui.view.welcome.WelcomeScreen
import com.example.snakegame.ui.view.game.GameScreen
import com.example.snakegame.ui.view.gameover.GameOverScreen
import com.example.snakegame.ui.view.stats.StatsScreen
import com.example.snakegame.viewmodel.LoginViewModel
import com.example.snakegame.viewmodel.NetworkViewModel
import com.example.snakegame.viewmodel.StatsViewModel

@Composable
fun NavGraph(gameViewModel: GameViewModel, loginViewModel: LoginViewModel, statsViewModel: StatsViewModel, networkViewModel: NetworkViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onNavigate = { navController.navigate(Screen.Welcome.route) }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                networkViewModel = networkViewModel,
                onPlayClick = { colorElegido ->
                    gameViewModel.onEvent(GameEvent.ResetGame)
                    gameViewModel.setSnakeColor(colorElegido)
                    navController.navigate(Screen.Game.route)
                },
                onStatsClick = {
                    navController.navigate(Screen.Stats.route)
                },
                onLogoutClick = {
                    loginViewModel.onLogout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Game.route) {
            GameScreen(
                gameViewModel = gameViewModel,
                onGameOver = { navController.navigate(Screen.GameOver.route) }
            )
        }

        composable(Screen.GameOver.route) {
            GameOverScreen(onRestart = {
                gameViewModel.onEvent(GameEvent.ResetGame)
                navController.navigate(Screen.Welcome.route)
            })
        }
        composable(Screen.Stats.route) {
            StatsScreen(
                statsViewModel = statsViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}