package com.gundogar.yazboz101


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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gundogar.yazboz101.ui.theme.LightGrayishPaper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YazbozScreen(
    viewModel: YazbozViewModel = viewModel(),
    s1: String,
    s2: String,
    s3: String,
    s4: String,
    ) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val state by viewModel.uiState.collectAsStateWithLifecycle()


    val scores = remember { mutableStateListOf<MutableList<Int>>() }

    Scaffold(
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            YazbozScreenContent(
                modifier = Modifier.fillMaxSize(),
                scores = state.scores,
                s1 = s1,
                s2 = s2,
                s3 = s3,
                s4 = s4
            )

            FloatingActionButton(
                onClick = { viewModel.onEvent(YazbozUiEvent.OpenSheet) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
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
                    .clickable { shareImageWithText(context) },
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
            title = "Kalan Puan Ekle",
            onDismiss = { viewModel.onEvent(YazbozUiEvent.CloseSheet) },
            onConfirm = { viewModel.onEvent(YazbozUiEvent.AddScores(it)) }
        )
    }

    if (state.showPenaltyDialog) {
        ScorePenaltyDialog(
            title = "Siler Puan Ekle",
            onDismiss = { viewModel.onEvent(YazbozUiEvent.CloseSheet) },
            onConfirm = { viewModel.onEvent(YazbozUiEvent.AddPenalties(it)) }
        )
    }
}

@Composable
fun YazbozScreenContent(
    modifier: Modifier = Modifier,
    scores: List<List<Int>>,
    s1: String,
    s2: String,
    s3: String,
    s4: String,
) {

    val list = listOf<String>(s1, s2, s3, s4)
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
            repeat(4) { playerIndex ->
                Column(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight()
                ) {

                    Text(text = list.get(playerIndex),color = androidx.compose.ui.graphics.Color.Black)

                    HorizontalDivider()
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(scores) { round ->
                            Text(
                                text = round[playerIndex].toString(),
                                fontSize = 18.sp,
                                color = androidx.compose.ui.graphics.Color.Black,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                    Text(
                        text = "Toplam: ${scores.sumOf { it[playerIndex] }}",
                        fontSize = 18.sp,
                        color = androidx.compose.ui.graphics.Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                if (playerIndex < 3) VerticalDivider()
            }
        }
    }
}

@Composable
fun ScorePenaltyDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (List<Int>) -> Unit
) {
    var inputValues by remember { mutableStateOf(List(4) { TextFieldValue("") }) }

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
                            val filteredText =
                                newValue.text.filter { it.isDigit() } // Sadece rakamları al
                            inputValues = inputValues.toMutableList()
                                .also { it[index] = newValue.copy(text = filteredText) }
                        },
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

