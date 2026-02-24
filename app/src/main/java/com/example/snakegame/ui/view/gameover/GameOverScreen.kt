package com.example.snakegame.ui.view.gameover

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GameOverScreen(onRestart: () -> Unit) {

    val infiniteTransition = rememberInfiniteTransition(label = "color")
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color(0xFF8B0000), // Rojo oscuro
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gameOverColor"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Partida Finalitzada",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text("Has chocado o has decidido terminar.")

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = onRestart) {
            Text("Volver al Menú Principal")
        }
    }
}