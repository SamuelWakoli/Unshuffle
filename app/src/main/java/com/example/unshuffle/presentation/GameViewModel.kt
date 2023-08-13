package com.example.unshuffle.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unshuffle.data.SCORE_REWARD
import com.example.unshuffle.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameStateModel())
    val uiState: StateFlow<GameStateModel> = _uiState.asStateFlow()

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String>? = null

    var unshuffledWord by mutableStateOf("")
        private set

    init {
        resetGame()
    }

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

    fun setWordInput(value: String) {
        unshuffledWord = value
    }

    fun onSubmit() {
        if (unshuffledWord.equals(currentWord, ignoreCase = true)) {

            _uiState.update { currentState ->
                currentState.copy(
                    currentWord = pickAndShuffle(),
                    isWordWrong = false,
                    score = currentState.score.plus(SCORE_REWARD),
                    currentRound = currentState.currentRound.inc(),
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isWordWrong = true
                )
            }
        }
        unshuffledWord = ""
    }

    fun onSkip() {
        unshuffledWord = ""
        _uiState.update { currentState ->
            currentState.copy(
                currentWord = pickAndShuffle(),
                isWordWrong = false,
                currentRound = currentState.currentRound.inc(),
            )
        }
    }
}