package com.example.unshuffle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.unshuffle.presentation.GameScreen
import com.example.unshuffle.ui.theme.UnshuffleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnshuffleTheme {
               GameScreen()
            }
        }
    }
}