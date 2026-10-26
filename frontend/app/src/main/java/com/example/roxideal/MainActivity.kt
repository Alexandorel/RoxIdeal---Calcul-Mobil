package com.example.roxideal

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = { RoxTopBar() }) { innerPadding ->
                    ProductsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoxTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.roxid_logo),
                    contentDescription = "Logo Roxid",
                    modifier = Modifier.size(40.dp)
                )
                Text("RoxIdeal", modifier = Modifier.padding(start = 12.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
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
                Text(
                    "${p.name} — ${p.price} lei (stoc: ${p.stock})",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            context.startActivity(
                                Intent(context, ProductDetailActivity::class.java)
                                    .putExtra("productId", p.id)
                            )
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}
