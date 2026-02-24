package com.example.snakegame.ui.view.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.snakegame.model.Direction
import com.example.snakegame.viewmodel.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel, onGameOver: () -> Unit) {

    val state by gameViewModel.state.collectAsState()

    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) onGameOver()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(Color(0xFF2D2D2D))
                .border(2.dp, Color.Gray)
        ) {
            state.snakeBody.forEach { pos ->
                Box(
                    modifier = Modifier
                        .offset(x = (pos.first * 15).dp, y = (pos.second * 15).dp)
                        .size(14.dp)
                        .background(Color(0xFF4CAF50))
                )
            }
            Box(
                modifier = Modifier
                    .offset(x = (state.food.first * 15).dp, y = (state.food.second * 15).dp)
                    .size(14.dp)
                    .background(Color.Red)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    gameViewModel.onDirectionChange(Direction.UP)
                },
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("↑")
            }
            Row {
                Button(
                    onClick = { gameViewModel.onDirectionChange(Direction.LEFT) },
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("←")
                }

                Button(
                    onClick = { gameViewModel.onDirectionChange(Direction.DOWN) },
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("↓")
                }

                Button(
                    onClick = { gameViewModel.onDirectionChange(Direction.RIGHT) },
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("→")
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedButton(
            onClick = { gameViewModel.forceGameOver() },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text("Terminar Partida")
        }
    }
}