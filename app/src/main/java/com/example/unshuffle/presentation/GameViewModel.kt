package com.example.unshuffle.presentation

import androidx.lifecycle.ViewModel
import com.example.unshuffle.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(GameStateModel())
    val UiState: StateFlow<GameStateModel> = _uiState.asStateFlow()

    lateinit var currentWord: String
    private var usedWords: MutableSet<String>? = null

    fun resetGame() {
        usedWords?.clear()
        currentWord = pickAndShuffle()
        _uiState.value = GameStateModel(currentWord = pickAndShuffle())
    }

    private fun pickAndShuffle(): String {
        currentWord = allWords.random()
        return when {
            usedWords?.contains(currentWord) == true -> pickAndShuffle()
            else -> {
                usedWords?.add(currentWord)
                val tempWord = currentWord.toCharArray()
                while (String(tempWord) == currentWord) tempWord.shuffle()
                String(tempWord)
            }
        }
    }
}