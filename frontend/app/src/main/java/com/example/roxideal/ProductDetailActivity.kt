package com.example.roxideal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.roxideal.data.remote.ApiClient
import com.example.roxideal.data.remote.ProductDto
import com.example.roxideal.ui.theme.RoxIdealTheme

class ProductDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val productId = intent.getLongExtra("productId", -1)
        setContent {
            RoxIdealTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { DetailTopBar(onBack = { finish() }) }
                ) { innerPadding ->
                    ProductDetailScreen(productId, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("Detalii produs") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = "Înapoi")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun ProductDetailScreen(productId: Long, modifier: Modifier = Modifier) {
    var product by remember { mutableStateOf<ProductDto?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(productId) {
        try {
            product = ApiClient.api.getProduct(productId)
        } catch (e: Exception) {
            error = e.message
        }
    }

    when {
        error != null -> Text("Eroare: $error", modifier = modifier)
        product == null -> Text("Se încarcă...", modifier = modifier)
        else -> {
            val p = product!!
            Column(modifier = modifier.padding(16.dp)) {
                Text(p.name, style = MaterialTheme.typography.headlineSmall)
                Text("${p.price} lei", style = MaterialTheme.typography.titleLarge)
                Text("Stoc: ${p.stock}")
                p.description?.let { Text(it, modifier = Modifier.padding(top = 16.dp)) }
            }
        }
    }
}
