package com.example.unshuffle.presentation

data class GameStateModel(
    val currentWord: String = "",
    val score: Int = 0,
    val currentRound: Int = 0,
    val isWordWrong: Boolean = false,
)
