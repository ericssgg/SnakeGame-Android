package com.example.snakegame.controller
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snakegame.viewmodel.GameViewModel
import com.example.snakegame.ui.view.login.LoginScreen
import com.example.snakegame.ui.view.welcome.WelcomeScreen
import com.example.snakegame.ui.view.game.GameScreen
import com.example.snakegame.ui.view.gameover.GameOverScreen

@Composable
fun NavGraph(gameViewModel: GameViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(onNavigate = { navController.navigate(Screen.Welcome.route) })
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(onNavigate = { navController.navigate(Screen.Game.route) })
        }
        composable(Screen.Game.route) {
            GameScreen(
                gameViewModel = gameViewModel,
                onGameOver = { navController.navigate(Screen.GameOver.route) }
            )
        }
        composable(Screen.GameOver.route) {
            GameOverScreen(onRestart = {
                gameViewModel.resetGame()
                navController.navigate(Screen.Welcome.route)
            })
        }
    }
}