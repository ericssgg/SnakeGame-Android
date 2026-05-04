package com.example.snakegame.ui.view.game

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.snakegame.R
import com.example.snakegame.model.Direction
import com.example.snakegame.model.GameEvent
import com.example.snakegame.viewmodel.GameViewModel
import kotlin.math.sqrt

@Composable
fun GameScreen(gameViewModel: GameViewModel, onGameOver: () -> Unit) {

    val state by gameViewModel.state.collectAsState()
    val context = LocalContext.current

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build()
            ).build()
    }

    val soundEat = remember { soundPool.load(context, R.raw.eat, 1) }
    val soundCrash = remember { soundPool.load(context, R.raw.crash, 1) }

    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.music)?.apply {
            isLooping = true
        }
    }

    val animatedBoardColor by animateColorAsState(
        targetValue = if (state.isGameOver) Color(0xFF5C1010) else Color(0xFF2D2D2D),
        animationSpec = tween(durationMillis = 1000),
        label = "boardColorAnim"
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                gameViewModel.onEvent(GameEvent.Pause)
                if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
            } else if (event == Lifecycle.Event.ON_RESUME) {
                if (!state.isStopped && !state.isGameOver) mediaPlayer?.start()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    DisposableEffect(Unit) {
        val listener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    if (mediaPlayer?.isPlaying == true) mediaPlayer.pause()
                }
                AudioManager.AUDIOFOCUS_GAIN -> {
                    if (!state.isStopped && !state.isGameOver) mediaPlayer?.start()
                }
            }
        }

        var focusRequestOreo: AudioFocusRequest? = null

        val result = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            focusRequestOreo = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setOnAudioFocusChangeListener(listener)
                .build()
            audioManager.requestAudioFocus(focusRequestOreo)
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED && !state.isGameOver) {
            mediaPlayer?.start()
        }

        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            soundPool.release()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && focusRequestOreo != null) {
                audioManager.abandonAudioFocusRequest(focusRequestOreo)
            } else {
                @Suppress("DEPRECATION")
                audioManager.abandonAudioFocus(listener)
            }
        }
    }

    // Sonido al comer
    LaunchedEffect(state.snakeBody.size) {
        if (state.snakeBody.size > 1) {
            soundPool.play(soundEat, 1f, 1f, 0, 0, 1f)
        }
    }

    // Sonido al morir y parar música
    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) {
            soundPool.play(soundCrash, 1f, 1f, 0, 0, 1f)
            if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
            onGameOver() // Navegar a pantalla final si es necesario
        } else if (!state.isStopped) {
            mediaPlayer?.start()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Cabecera Puntos y Tiempo
        Row(
            modifier = Modifier.fillMaxWidth(0.95f).padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Puntos: ${state.snakeBody.size}", color = Color.White)
            Text("Tiempo: ${state.timeElapsed} s", color = Color.White)
        }

        // Tablero Adaptativo
        LazyVerticalGrid(
            columns = GridCells.Fixed(20),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(1f)
                .background(animatedBoardColor)
                .border(3.dp, Color.Gray)
                .padding(3.dp),
            userScrollEnabled = false
        ) {
            items(400) { index ->
                val x = index % 20
                val y = index / 20
                val currentPos = Pair(x, y)

                val isFood = state.food == currentPos
                val isSnakeHead = state.snakeBody.firstOrNull() == currentPos
                val isSnakeBody = state.snakeBody.contains(currentPos) && !isSnakeHead

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(0.5.dp)
                        .background(
                            when {
                                isFood -> Color.Red
                                isSnakeHead -> state.snakeColor
                                isSnakeBody -> state.snakeColor.copy(alpha = 0.7f)
                                else -> Color.Transparent
                            }
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Controles de Dirección
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { gameViewModel.onEvent(GameEvent.Move(Direction.UP)) },
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("↑")
            }
            Row {
                Button(
                    onClick = { gameViewModel.onEvent(GameEvent.Move(Direction.LEFT)) },
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("←")
                }

                Button(
                    onClick = { gameViewModel.onEvent(GameEvent.Move(Direction.DOWN)) },
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("↓")
                }

                Button(
                    onClick = { gameViewModel.onEvent(GameEvent.Move(Direction.RIGHT)) },
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("→")
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Botón Terminar
        OutlinedButton(
            onClick = { gameViewModel.onEvent(GameEvent.ForceGameOver) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text("Terminar Partida")
        }
    }

    if (state.isStopped && !state.isGameOver) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("JUEGO PAUSADO", color = Color.White)
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    gameViewModel.onEvent(GameEvent.Resume)
                    mediaPlayer?.start()
                }) {
                    Text("Reanudar")
                }
            }
        }
    }
}