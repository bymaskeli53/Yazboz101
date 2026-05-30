package com.gundogar.yazboz101.ui.screens.winner

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gundogar.yazboz101.data.Player
import com.gundogar.yazboz101.ui.Screen
import com.gundogar.yazboz101.util.shareResultCard

private val Gold = Color(0xFFD4B100)
private val Silver = Color(0xFFB0BEC5)
private val Bronze = Color(0xFFCD7F32)
private val WhatsAppGreen = Color(0xFF25D366)

@Composable
fun WinnerScreen(
    players: List<Player>,
    navController: NavHostController
) {
    val context = LocalContext.current
    // 101'de en düşük toplam kazanır.
    val ranked = players.sortedBy { it.scores.sum() }
    val winner = ranked.firstOrNull()

    // Giriş animasyonu: alpha 0 -> 1, ölçek 0.8 -> 1.
    var started by remember { mutableStateOf(false) }
    val enter by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "winnerEnter"
    )
    LaunchedEffect(Unit) { started = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1D2671), Color(0xFFC33764))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "OYUN BİTTİ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Kazanan vurgusu (animasyonlu)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                alpha = enter
                val scale = 0.8f + 0.2f * enter
                scaleX = scale
                scaleY = scale
            }
        ) {
            Text(text = "🏆", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${winner?.name ?: "-"} Kazandı!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Gold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tebrikler!",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "SKOR TABLOSU",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ranked.forEachIndexed { index, player ->
                RankingRow(rank = index, player = player)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // WhatsApp'ta paylaş
        Button(
            onClick = { shareResultCard(context, players) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WhatsAppGreen,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Filled.Share, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "WhatsApp'ta Paylaş", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Ana menüye dön
        OutlinedButton(
            onClick = {
                navController.navigate(Screen.MenuScreen) {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Gold),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold)
        ) {
            Text(text = "Ana Menüye Dön", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun RankingRow(rank: Int, player: Player) {
    val medal = when (rank) {
        0 -> "🥇"
        1 -> "🥈"
        2 -> "🥉"
        else -> "${rank + 1}."
    }
    val nameColor = when (rank) {
        0 -> Gold
        1 -> Silver
        2 -> Bronze
        else -> Color.White
    }
    val nameWeight = if (rank == 0) FontWeight.Black else if (rank <= 2) FontWeight.Bold else FontWeight.Normal
    val nameSize = if (rank == 0) 18.sp else 16.sp

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$medal ${player.name}",
                fontSize = nameSize,
                fontWeight = nameWeight,
                color = nameColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${player.scores.sum()} puan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
