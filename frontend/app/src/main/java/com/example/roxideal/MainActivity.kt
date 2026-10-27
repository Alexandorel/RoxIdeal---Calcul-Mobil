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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.roxid_logo),
                    contentDescription = "Logo Roxid",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Text("RoxIdeal", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp))
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

private const val LOCAL_NETWORK_PERMISSION = "android.permission.ACCESS_LOCAL_NETWORK"

@Composable
fun ProductsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var products by remember { mutableStateOf<List<ProductDto>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var query by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }  // null = „Toate”
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
        else -> {
            val categories = products!!.mapNotNull { it.category }.distinct()
            val visible = products!!.filter {
                it.name.contains(query, ignoreCase = true) &&
                    (selectedCategoryId == null || it.category?.id == selectedCategoryId)
            }
            Column(modifier = modifier) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Caută produse...") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                if (categories.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategoryId == null,
                                onClick = { selectedCategoryId = null },
                                label = { Text("Toate") }
                            )
                        }
                        items(categories) { c ->
                            FilterChip(
                                selected = selectedCategoryId == c.id,
                                onClick = { selectedCategoryId = c.id },
                                label = { Text(c.name) }
                            )
                        }
                    }
                }
                LazyColumn {
                    items(visible) { p ->
                        Card(
                            onClick = {
                                context.startActivity(
                                    Intent(context, ProductDetailActivity::class.java)
                                        .putExtra("productId", p.id)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                // Placeholder until product images are loaded from imageUrl
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                )
                                Column(modifier = Modifier.padding(start = 12.dp)) {
                                    Text(p.name, style = MaterialTheme.typography.titleMedium)
                                    Text("${p.price} lei", style = MaterialTheme.typography.titleLarge)
                                    if (p.stock == 0) {
                                        Text(
                                            "Stoc epuizat",
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
