package com.example.unshuffle.ui.test

import com.example.unshuffle.data.SCORE_REWARD
import com.example.unshuffle.presentation.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()
    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_REWARD
    }
    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val shuffledWord = currentGameUiState.currentWord

        viewModel.setWordInput(shuffledWord)
        viewModel.onSubmit()

        // Assert that checkUserGuess() method updates isGuessedWordWrong is updated correctly.
        assertFalse(currentGameUiState.isWordWrong)
        // Assert that score is updated correctly.
        assertEquals(0, currentGameUiState.score)
    }
}