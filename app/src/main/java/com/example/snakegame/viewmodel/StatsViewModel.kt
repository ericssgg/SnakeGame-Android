package com.example.snakegame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.dao.SnakeDAO
import com.example.snakegame.model.GameResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(private val snakeDao: SnakeDAO) : ViewModel() {

    private val _topScores = MutableStateFlow<List<GameResult>>(emptyList())
    val topScores = _topScores.asStateFlow()

    init {
        viewModelScope.launch {
            snakeDao.getTopScores().collect { scores ->
                _topScores.value = scores
            }
        }
    }
}