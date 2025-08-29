package dev.terry1921.nenektrivia.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import dev.terry1921.nenektrivia.ui.theme.NenekTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NenekTheme(darkTheme = false) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Text(
                        text = "Bienvenido a Nenek Trivia 🎉",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
    }
}
