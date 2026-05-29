package com.gundogar.yazboz101.ui.screens.previous

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gundogar.yazboz101.data.GameMode
import com.gundogar.yazboz101.data.Player
import com.gundogar.yazboz101.data.YazbozItem
import com.gundogar.yazboz101.data.formatDateTime

private val ScreenGradient = Brush.verticalGradient(
    listOf(Color(0xFFC33764), Color(0xFF1D2671))
)
private val Gold = Color(0xFFD4B100)
private val CardBackground = Color(0xFF2A2A3D).copy(alpha = 0.9f)
private val LoserTint = Color(0xFFE57373).copy(alpha = 0.18f)
private val LoserText = Color(0xFFFF8A80)

@Composable
fun PreviousGamesScreen(
    viewModel: PreviousGamesViewModel = hiltViewModel(),
    onResumeGame: (YazbozItem) -> Unit = {}
) {
    val games by viewModel.games.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var gameToDelete by remember { mutableStateOf<YazbozItem?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenGradient)
    ) {
        if (games.isEmpty()) {
            EmptyScreen()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(games, key = { it.id }) { game ->
                    GameCard(
                        game = game,
                        modifier = Modifier.animateItem(),
                        onClick = { onResumeGame(game) },
                        onClickDelete = {
                            gameToDelete = it
                            showDialog = true
                        }
                    )
                }
            }
        }

        if (showDialog && gameToDelete != null) {
            DeleteConfirmationDialog(
                onConfirm = {
                    viewModel.deleteGame(gameToDelete!!)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}


@Composable
fun GameCard(
    game: YazbozItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onClickDelete: (YazbozItem) -> Unit
) {
    // Görünürken yumuşak bir fade-in.
    var visible by remember { mutableStateOf(false) }
    val cardAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "cardAlpha"
    )
    LaunchedEffect(game.id) { visible = true }

    // 101'de en düşük toplam kazanır.
    val ranked = game.players.sortedBy { it.scores.sum() }
    val loserSum = ranked.lastOrNull()?.scores?.sum()
    val roundCount = game.players.firstOrNull()?.scores?.size ?: 0

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = CardBackground,
        shadowElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Üst satır: tarih + sil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDateTime(game.createdAt),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold
                )

                IconButton(onClick = { onClickDelete(game) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Sil",
                        tint = Color(0xFFE57373)
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Gold.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sıralama listesi
            ranked.forEachIndexed { index, player ->
                val isWinner = index == 0
                val isLoser = ranked.size > 1 && !isWinner && player.scores.sum() == loserSum
                PlayerRankRow(
                    rank = index,
                    player = player,
                    isWinner = isWinner,
                    isLoser = isLoser
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Alt satır: oynanan el sayısı + oyun modu rozetleri
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoChip(text = "🎲 $roundCount el")
                InfoChip(
                    text = if (game.gameMode == GameMode.TEAM) "👥 Takım" else "👤 Bireysel"
                )
            }
        }
    }
}


@Composable
private fun PlayerRankRow(
    rank: Int,
    player: Player,
    isWinner: Boolean,
    isLoser: Boolean
) {
    val medal = when (rank) {
        0 -> "🥇"
        1 -> "🥈"
        2 -> "🥉"
        else -> "${rank + 1}."
    }
    val nameColor = when {
        isWinner -> Gold
        isLoser -> LoserText
        else -> Color.White
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(
                color = if (isLoser) LoserTint else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = medal,
            fontSize = 18.sp,
            modifier = Modifier.size(width = 32.dp, height = 24.dp)
        )
        Text(
            text = player.name,
            fontSize = 16.sp,
            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
            color = nameColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${player.scores.sum()}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = nameColor
        )
    }
}


@Composable
private fun InfoChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Gold.copy(alpha = 0.18f)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Gold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}


@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFE57373)
            )
        },
        title = { Text("Oyunu Sil") },
        text = { Text("Bu oyunu silmek istediğinizden emin misiniz?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sil", color = Color(0xFFE57373), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal", color = Color.Gray)
            }
        }
    )
}


@Composable
fun EmptyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Emoji'nin arkasında yumuşak bir parıltı.
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Gold.copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
            Text(text = "🃏", fontSize = 96.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Henüz oyun yok",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "İlk oyununuzu oynayın!",
            fontSize = 15.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}
