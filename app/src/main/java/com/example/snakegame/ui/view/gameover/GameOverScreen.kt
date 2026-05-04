package com.example.snakegame.ui.view.gameover

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

@Composable
fun GameOverScreen(onRestart: () -> Unit) {

    val context = LocalContext.current

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val gForce = sqrt(((x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)).toDouble()).toFloat()

                if (gForce > 2.1f) {
                    onRestart()
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI)

        onDispose { sensorManager.unregisterListener(sensorEventListener) }
    }
    val infiniteTransition = rememberInfiniteTransition(label = "color")
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color(0xFF8B0000),
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
            color = color
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text("Has chocado o has decidido terminar.")

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = onRestart) {
            Text("Volver al Menú Principal")
        }
    }
}