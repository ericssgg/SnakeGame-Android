package com.example.snakegame.controller

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Welcome: Screen("welcome")
    object Game : Screen("game")
    object GameOver : Screen("gameover")
}