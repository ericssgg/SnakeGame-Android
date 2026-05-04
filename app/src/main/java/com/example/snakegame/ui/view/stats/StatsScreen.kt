package com.example.snakegame.ui.view.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.snakegame.viewmodel.StatsViewModel

@Composable
fun StatsScreen(statsViewModel: StatsViewModel, onBack: () -> Unit) {

    val scores by statsViewModel.topScores.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Top 10 Puntuaciones", color = Color.White, style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(24.dp))

        if (scores.isEmpty()) {
            Text("Aún no hay partidas guardadas.", color = Color.Gray)
        } else {
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                itemsIndexed(scores) { index, result ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${index + 1}. ${result.playerName}", color = Color.White)
                            Text("${result.score} pts", color = Color.Green, style = MaterialTheme.typography.titleMedium)
                            Text(result.date, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
            Text("Volver al Menú", color = Color.White)
        }
    }
}