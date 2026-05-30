package com.gundogar.yazboz101.ui.screens.yazboz


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gundogar.yazboz101.data.GameMode
import com.gundogar.yazboz101.data.Player
import com.gundogar.yazboz101.ui.Screen

@Composable
fun YazbozScreen(
    viewModel: YazbozViewModel = hiltViewModel(),
    players: List<Player>,
    gameMode: GameMode = GameMode.INDIVIDUAL,
    gameId: Int = 0,
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Bitir'e basıldığında doldurulur; dialog kapandıktan SONRA WinnerScreen'e geçilir.
    var pendingWinner by remember { mutableStateOf<List<Player>?>(null) }

    // Kayıtlı bir oyun açıldıysa skorlarını geri yükle (yeni oyunda etkisizdir).
    LaunchedEffect(Unit) {
        viewModel.onEvent(YazbozUiEvent.LoadGame(players))
    }

    // Navigasyonu bir sonraki frame'e ertele: önce dialog DOM'dan kalkar, sonra geçiş olur.
    LaunchedEffect(pendingWinner) {
        pendingWinner?.let { winners ->
            navController.navigate(Screen.WinnerScreen(winners))
            pendingWinner = null
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            YazbozScreenContent(
                modifier = Modifier.fillMaxSize(),
                scores = state.scores,
                players = players,
                gameMode = gameMode
            )

            // Üst butonların arkasına derinlik veren koyu -> şeffaf degrade.
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0x33000000), Color.Transparent)
                        )
                    )
            )

            // Oyunu bitir butonu (sağ üst)
            val finishShape = RoundedCornerShape(50)
            Button(
                onClick = { viewModel.onEvent(YazbozUiEvent.ShowFinishDialog) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .height(46.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = finishShape,
                        spotColor = Color(0xFFD4B100),
                        ambientColor = Color(0xFFD4B100)
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFFF2D04B), Color(0xFFD4B100))
                        ),
                        shape = finishShape
                    ),
                shape = finishShape,
                contentPadding = PaddingValues(start = 6.dp, end = 18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                elevation = null
            ) {
                // Beyaz halka içinde onay ikonu
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(Color.White.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Oyunu Bitir",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp
                )
            }

            FloatingActionButton(
                onClick = { viewModel.onEvent(YazbozUiEvent.OpenSheet) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                containerColor = Color(0xFF384247),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Ekle", tint = Color(0xFFD4B100))
            }
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

    if (state.showFinishDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(YazbozUiEvent.CloseFinishDialog) },
            title = { Text(text = "Oyunu Bitir") },
            text = { Text(text = "Oyunu bitirmek istediğinizden emin misiniz?") },
            confirmButton = {
                TextButton(onClick = {
                    val updatedPlayers = players.mapIndexed { index, player ->
                        player.copy(scores = state.scores.map { it.getOrNull(index) ?: 0 })
                    }
                    viewModel.onEvent(YazbozUiEvent.SaveGame(updatedPlayers, gameMode, gameId))
                    // Önce dialog'u kapat, navigasyonu LaunchedEffect'e bırak.
                    viewModel.onEvent(YazbozUiEvent.CloseFinishDialog)
                    pendingWinner = updatedPlayers
                }) {
                    Text(text = "Bitir")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(YazbozUiEvent.CloseFinishDialog) }) {
                    Text(text = "Devam Et")
                }
            }
        )
    }
}

@Composable
fun YazbozScreenContent(
    modifier: Modifier = Modifier,
    scores: List<List<Int>>,
    players: List<Player>,
    gameMode: GameMode = GameMode.INDIVIDUAL,
) {
    val paperColor = Color(0xFFFDF6E3)
    val ruleColor = Color(0xFFE0D8C8)
    val slate = Color(0xFF384247)
    val gold = Color(0xFFD4B100)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(paperColor)
            // Üstte, üzerine binen Paylaş / Oyunu bitir butonları için boşluk bırak.
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 64.dp)
    ) {
        // Başlık
        Text(
            text = "YAZBOZ",
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 6.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // İnce altın çizgi
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp)
                .width(80.dp)
                .height(2.dp)
                .background(gold)
        )

        Text(
            text = if (gameMode == GameMode.TEAM) "Takım Oyunu" else "Bireysel Oyun",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            players.forEachIndexed { playerIndex, player ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(0.92f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // İsim çipi
                    Box(
                        modifier = Modifier
                            .background(slate, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = player.name,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .drawBehind {
                                // Silik defter çizgileri
                                val spacing = 32.dp.toPx()
                                val stroke = 1.dp.toPx()
                                var y = spacing
                                while (y < size.height) {
                                    drawLine(
                                        color = ruleColor,
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = stroke
                                    )
                                    y += spacing
                                }
                            }
                    ) {
                        itemsIndexed(scores) { rowIndex, round ->
                            val value = round.getOrNull(playerIndex)
                            val rowBackground =
                                if (rowIndex % 2 == 0) Color(0x08000000) else Color.Transparent
                            val scoreColor = when {
                                value == null -> Color.Black
                                value < 0 -> Color(0xFFE53935)
                                value > 0 -> Color(0xFF2E7D32)
                                else -> Color.Black
                            }
                            Text(
                                text = value?.toString() ?: "-",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 16.sp,
                                color = scoreColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(rowBackground)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Toplam satırı
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                slate,
                                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Toplam: ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${scores.sumOf { it.getOrNull(playerIndex) ?: 0 }}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
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


