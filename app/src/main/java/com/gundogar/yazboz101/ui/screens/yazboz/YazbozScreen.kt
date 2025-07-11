package com.gundogar.yazboz101.ui.screens.yazboz


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gundogar.yazboz101.data.Player
import com.gundogar.yazboz101.util.shareImageWithText
import com.gundogar.yazboz101.ui.theme.LightGrayishPaper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YazbozScreen(
    viewModel: YazbozViewModel = hiltViewModel(),
    players: List<Player>,
    navController: NavHostController
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            YazbozScreenContent(
                modifier = Modifier.fillMaxSize(),
                scores = state.scores,
                players = players
            )

            Button(onClick = {
                val updatedPlayers = players.mapIndexed { index, player ->
                    player.copy(scores = state.scores.map { it.getOrNull(index) ?: 0 })
                }
                viewModel.onEvent(YazbozUiEvent.SaveGame(updatedPlayers))
                navController.popBackStack()
                Toast.makeText(context, "Oyun kaydedildi", Toast.LENGTH_SHORT).show()

            }, modifier = Modifier.align(Alignment.TopEnd).padding(top = 8.dp, end = 8.dp)) {
                Text(text = "Oyunu bitir")
            }

            FloatingActionButton(
                onClick = { viewModel.onEvent(YazbozUiEvent.OpenSheet) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Ekle")
            }

            Icon(
                Icons.Filled.Share,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .width(60.dp)
                    .height(60.dp)
                    .clickable {
                        // Oyuncuların skorlarını güncelle
                     shareImageWithText(context)
                    },
                tint = androidx.compose.ui.graphics.Color.Black,
                contentDescription = null
            )
        }

    }

    if (state.isSheetOpen) {
        YazbozSheet(
            onScoreClick = { viewModel.onEvent(YazbozUiEvent.ShowScoreDialog) },
            onPenaltyClick = { viewModel.onEvent(YazbozUiEvent.ShowPenaltyDialog) },
            onDismiss = { viewModel.onEvent(YazbozUiEvent.CloseSheet) }
        )
    }

    if (state.showScoreDialog) {
        ScorePenaltyDialog(
            players = players,
            title = "Kalan Puan Ekle",
            playerCount = players.size,
            onDismiss = { viewModel.onEvent(YazbozUiEvent.CloseSheet) },
            onConfirm = { viewModel.onEvent(YazbozUiEvent.AddScores(it)) }
        )
    }

    if (state.showPenaltyDialog) {
        ScorePenaltyDialog(
            players = players,
            title = "Siler Puan Ekle",
            playerCount = players.size,
            onDismiss = { viewModel.onEvent(YazbozUiEvent.CloseSheet) },
            onConfirm = { viewModel.onEvent(YazbozUiEvent.AddPenalties(it)) }
        )
    }
}

@Composable
fun YazbozScreenContent(
    modifier: Modifier = Modifier,
    scores: List<List<Int>>,
    players: List<Player>,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(LightGrayishPaper)
            .padding(16.dp)
    ) {
        Text(
            text = "YAZBOZ",
            fontSize = 30.sp,
            color = androidx.compose.ui.graphics.Color.Black,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            players.forEachIndexed { playerIndex, player ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(0.92f)

                ) {
                    Text(
                        text = player.name,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = androidx.compose.ui.graphics.Color.Black
                    )

                    HorizontalDivider()

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(scores) { round ->
                            Text(
                                text = round.getOrNull(playerIndex)?.toString() ?: "-",
                                fontSize = 18.sp,
                                color = androidx.compose.ui.graphics.Color.Black,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    Text(
                        text = "Toplam: ${scores.sumOf { it.getOrNull(playerIndex) ?: 0 }}",
                        fontSize = 18.sp,
                        color = androidx.compose.ui.graphics.Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                if (playerIndex < players.lastIndex) {
                    VerticalDivider(modifier = Modifier.fillMaxHeight(0.92f))
                }
            }
        }
    }
}


@Composable
fun ScorePenaltyDialog(
    players: List<Player>,
    title: String,
    playerCount: Int,
    onDismiss: () -> Unit,
    onConfirm: (List<Int>) -> Unit
) {
    var inputValues by remember { mutableStateOf(List(playerCount) { TextFieldValue("") }) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column {
                inputValues.forEachIndexed { index, value ->
                    OutlinedTextField(
                        value = value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { newValue ->
                            val filteredText = newValue.text.filter { it.isDigit() }
                            inputValues = inputValues.toMutableList()
                                .also { it[index] = newValue.copy(text = filteredText) }
                        },
                        label = { Text(players[index].name) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val scores = inputValues.map { it.text.toIntOrNull() ?: 0 }
                onConfirm(scores)
                onDismiss()
            }) {
                Text("Ekle")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}


