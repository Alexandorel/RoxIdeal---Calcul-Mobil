package com.example.exercitiu2

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
import com.example.exercitiu2.ui.theme.Exercitiu2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exercitiu2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BusinessCard(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BusinessCard(modifier: Modifier = Modifier) {
    var profile by remember { mutableStateOf("Niciun profil încărcat") }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = profile)
        Button(onClick = {
            val student = Student(
                nume = "Alexandru Vint",
                limbajPreferat = "Kotlin",
                anStudiu = 4
            )
            profile = """
                Nume: ${student.nume}
                Limbaj preferat: ${student.limbajPreferat}
                An de studiu: ${student.anStudiu}
            """.trimIndent()
        }) {
            Text("Încarcă Profil")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessCardPreview() {
    Exercitiu2Theme {
        BusinessCard()
    }
}