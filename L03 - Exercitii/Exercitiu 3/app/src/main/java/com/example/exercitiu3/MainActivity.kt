package com.example.exercitiu3

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exercitiu3.ui.theme.Exercitiu3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exercitiu3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MagicFilter(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MagicFilter(modifier: Modifier = Modifier) {
    val numbers = (1..10).toList()
    var result by remember { mutableStateOf("Result: -") }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Raw data: " + numbers.joinToString(separator = ", "))
        Text(result)
        Button(onClick = {
            result = "Result: " + numbers
                .filter { it % 2 == 0 }
                .map { it * 10 }
                .joinToString(separator = ", ")
        }) {
            Text("Process")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MagicFilterPreview() {
    Exercitiu3Theme {
        MagicFilter()
    }
}
