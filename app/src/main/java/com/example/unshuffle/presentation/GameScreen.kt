package com.example.unshuffle.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unshuffle.data.MAX_ROUNDS
import com.example.unshuffle.data.MAX_SCORE


@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUIState by gameViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ScoreBoard(gameUIState.score)
        Spacer(modifier = Modifier.height(20.dp))
        GameLayout(
            round = gameUIState.currentRound,
            currentWord = gameUIState.currentWord,
            wordInput = gameViewModel.unshuffledWord,
            onWordInputChange = { gameViewModel.setWordInput(it) },
            onSubmitWord = { gameViewModel.onSubmit() },
            isError = gameUIState.isWordWrong
        )
        Spacer(modifier = Modifier.height(20.dp))
        GameController(
            onSubmitWord = { gameViewModel.onSubmit() },
            onSkip = { gameViewModel.onSkip() })
        Spacer(modifier = Modifier.height(80.dp))
        if (gameUIState.currentRound >= MAX_ROUNDS) GameScreenDialog(
            score = gameUIState.score,
            onPlayAgain = { gameViewModel.resetGame() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreen()
}

@Composable
fun GameScreenDialog(
    score: Int,
    onPlayAgain: () -> Unit
) {
    val activity = (LocalContext.current as Activity)
    AlertDialog(
        title = { Text(text = "Congratulations!") },
        text = { Text(text = "You have scored: $score") },
        onDismissRequest = { /*an empty lambda prevents user from dismissing dialog by clicking outside*/ },
        confirmButton = { TextButton(onClick = { onPlayAgain() }) { Text(text = "Play Again") } },
        dismissButton = { TextButton(onClick = { activity.finish() }) { Text(text = "Exit") } }
    )
}

@Composable
fun ScoreBoard(score: Int) {
    Card(
        Modifier
            .padding(8.dp)
            .width(100.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(text = "Score", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "$score",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScoreBoardPreview() {
    ScoreBoard(100)
}

@Composable
fun GameController(onSubmitWord: () -> Unit, onSkip: () -> Unit) {
    Column(Modifier.padding(8.dp)) {
        Button(
            onClick = onSubmitWord,
            modifier = Modifier.width(200.dp),
        ) {
            Text(text = "Submit")
        }
        OutlinedButton(
            onClick = onSkip,
            modifier = Modifier.width(200.dp),
        ) {
            Text(text = "Skip")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameLayout(
    round: Int,
    currentWord: String,
    wordInput: String,
    onWordInputChange: (String) -> Unit,
    onSubmitWord: () -> Unit,
    isError: Boolean
) {
    Card {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Card(
                modifier = Modifier.align(alignment = Alignment.End),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {

                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        text = "Round",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        fontSize = 8.sp,
                    )
                    Text(fontSize = 10.sp, text = "$round/$MAX_ROUNDS")
                }
            }
            Text(
                text = "Unshuffle",
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Use all letters below to form a word",
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentWord,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = wordInput,
                onValueChange = onWordInputChange,
                shape = MaterialTheme.shapes.medium,
                label = {
                    when {
                        isError -> Text(text = "Please try again")
                        else -> Text(text = "Enter your word")
                    }
                },
                isError = isError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onSubmitWord() }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}
