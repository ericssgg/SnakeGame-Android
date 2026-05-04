package com.example.snakegame.ui.view.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.snakegame.viewmodel.NetworkViewModel

@Composable
fun WelcomeScreen(
    onPlayClick: (Color) -> Unit,
    onStatsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    networkViewModel: NetworkViewModel
) {
    var selectedColor by remember { mutableStateOf(Color(0xFF4CAF50)) }

    // Obtenemos el mensaje de internet desde el ViewModel
    val networkMessage by networkViewModel.serverMessage.collectAsState()

    val snakeColors = listOf(
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFFC107),
        Color(0xFFE91E63)
    )

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ✅ 1. La tarjeta de Red (Ahora sí muestra el mensaje de internet)
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D)),
            modifier = Modifier.fillMaxWidth(0.9f).padding(top = 16.dp, bottom = 30.dp)
        ) {
            Text(
                text = networkMessage,
                color = if (networkMessage.contains("Sin conexión")) Color.Red else Color.Green,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // ✅ 2. Título y Subtítulo (Fuera de la tarjeta)
        Text("Snake Game", color = Color.White, style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("¡Prepárate para jugar!", color = Color.Gray, style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(50.dp))

        Text("Elige el color de tu serpiente:", color = Color.White, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            snakeColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (selectedColor == color) 4.dp else 0.dp,
                            color = if (selectedColor == color) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { selectedColor = color }
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = { onPlayClick(selectedColor) },
            modifier = Modifier.fillMaxWidth(0.7f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
        ) {
            Text("JUGAR", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onStatsClick,
            modifier = Modifier.fillMaxWidth(0.7f).height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("VER ESTADÍSTICAS")
        }

        Spacer(Modifier.height(30.dp))

        TextButton(onClick = onLogoutClick) {
            Text("Cerrar Sesión", color = Color(0xFFE53935))
        }
    }
}