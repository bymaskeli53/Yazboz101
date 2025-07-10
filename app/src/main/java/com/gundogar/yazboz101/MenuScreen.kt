package com.gundogar.yazboz101

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    onNavigateToYazboz: (List<Player>) -> Unit,
    onNavigateToPreviousGames: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val isimler = remember { mutableStateListOf("", "", "", "") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFEEEDEB), Color(0xFFD6D3CE))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_main_yazboz),
                contentDescription = "Oyun Logosu",
                modifier = Modifier
                    .height(200.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "YAZBOZ 101",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C2C2C)
                )
            )

            MenuButton(
                text = "Yeni Oyun",
                icon = Icons.Default.PlayArrow,
                onClick = { showDialog = true }
            )

            MenuButton(
                text = "Önceki Oyunlarım",
                icon = Icons.Outlined.Home,
                onClick = onNavigateToPreviousGames
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Oyuncu İsimlerini Girin") },
                text = {
                    Column {
                        isimler.forEachIndexed { index, value ->
                            OutlinedTextField(
                                value = value,
                                onValueChange = { isimler[index] = it },
                                label = { Text("Oyuncu ${index + 1}") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (isimler.all { it.isNotBlank() }) {
                            val players = isimler.map { Player(name = it, scores = listOf(0, 0, 0, 0)) }
                            onNavigateToYazboz(players)
                            showDialog = false
                        } else {
                            Toast.makeText(
                                context,
                                "Lütfen tüm oyuncu isimlerini girin.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Text("Başla")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("İptal")
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

