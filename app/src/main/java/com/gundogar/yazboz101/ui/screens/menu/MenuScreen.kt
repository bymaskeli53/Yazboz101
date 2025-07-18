package com.gundogar.yazboz101.ui.screens.menu

import android.widget.Toast
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gundogar.yazboz101.data.Player
import com.gundogar.yazboz101.R
import com.gundogar.yazboz101.ui.PlayerNameDialog

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
                    listOf(Color(0xFFC33764), Color(0xFF1D2671))
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
                    color = Color(0xFFCFECEC)
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
            PlayerNameDialog(
                isimler = isimler,
                onDismiss = { showDialog = false },
                onConfirm = { players ->
                    onNavigateToYazboz(players)
                }
            )
        }
    }
}

