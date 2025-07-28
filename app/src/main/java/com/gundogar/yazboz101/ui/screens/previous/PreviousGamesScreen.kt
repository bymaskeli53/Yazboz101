package com.gundogar.yazboz101.ui.screens.previous

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gundogar.yazboz101.data.YazbozItem
import com.gundogar.yazboz101.data.formatDateTime

@Composable
fun PreviousGamesScreen(viewModel: PreviousGamesViewModel = hiltViewModel()) {
    val games by viewModel.games.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var gameToDelete by remember { mutableStateOf<YazbozItem?>(null) }

    if (games.isEmpty()) {
        EmptyScreen()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(games) { game ->
                GameCard(game = game, onClickDelete = {
                    gameToDelete = it
                    showDialog = true
                })
            }
        }

        if (showDialog && gameToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Oyunu Sil") },
                text = { Text("Bu oyunu silmek istediğinizden emin misiniz?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteGame(gameToDelete!!)
                            showDialog = false
                        }
                    ) {
                        Text("Sil")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("İptal")
                    }
                }
            )
        }
    }
}


@Composable
fun GameCard(
    game: YazbozItem,
    onClickDelete: (YazbozItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatDateTime(game.createdAt),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )

            IconButton(onClick = { onClickDelete(game) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Sil",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val sortedPlayers = game.players.sortedBy { it.scores.sum() }

        sortedPlayers.forEachIndexed { index, player ->
            Text(
                text = "${index + 1}. ${player.name}: ${player.scores.joinToString(", ")} (Toplam: ${player.scores.sum()})",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
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
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Henüz kayıtlı bir oyun yok",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

