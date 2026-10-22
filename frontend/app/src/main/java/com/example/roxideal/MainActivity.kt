package com.example.roxideal

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.roxideal.data.remote.ApiClient
import com.example.roxideal.data.remote.ProductDto
import com.example.roxideal.ui.theme.RoxIdealTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoxIdealTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProductsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

private const val LOCAL_NETWORK_PERMISSION = "android.permission.ACCESS_LOCAL_NETWORK"

@Composable
fun ProductsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var products by remember { mutableStateOf<List<ProductDto>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    // Android 17+ blocks private addresses like 10.0.2.2 without this runtime permission
    var granted by remember {
        mutableStateOf(
            Build.VERSION.SDK_INT < 37 ||
                ContextCompat.checkSelfPermission(context, LOCAL_NETWORK_PERMISSION) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
        if (ok) granted = true else error = "Permisiunea de acces la rețeaua locală a fost refuzată"
    }

    LaunchedEffect(granted) {
        if (!granted) {
            permissionLauncher.launch(LOCAL_NETWORK_PERMISSION)
            return@LaunchedEffect
        }
        try {
            products = ApiClient.api.getProducts()
        } catch (e: Exception) {
            error = e.message
        }
    }

    when {
        error != null -> Text("Eroare: $error", modifier = modifier)
        products == null -> Text("Se încarcă...", modifier = modifier)
        else -> LazyColumn(modifier = modifier) {
            items(products!!) { p ->
                Text("${p.name} — ${p.price} lei (stoc: ${p.stock})")
            }
        }
    }
}
