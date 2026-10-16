package com.example.exercitiu1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exercitiu1.ui.theme.Exercitiu1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exercitiu1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MessageOfTheDay(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MessageOfTheDay(modifier: Modifier = Modifier) {
    val messages = listOf(
        "Have a great day!",
        "You can do it!",
        "Keep going!",
        "Stay positive!",
        "Never give up!"
    )

    var message by remember { mutableStateOf("Press the button for a message") }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)
        Button(onClick = { message = messages.random() }) {
            Text("Inspire me!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageOfTheDayPreview() {
    Exercitiu1Theme {
        MessageOfTheDay()
    }
}
