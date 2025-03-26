package com.gundogar.yazboz101

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YazbozScreen(modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }

    var showScoreDialog by remember { mutableStateOf(false) }
    var showPenaltyDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isSheetOpen = true },
                ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        YazbozScreenContent(
            modifier = modifier.padding(paddingValues)
        )
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    // Skor ekleme işlemi burada yapılabilir
                    showScoreDialog = true
                    isSheetOpen = false
                }) {
                    Text("Skor Ekle")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // Ceza ekleme işlemi burada yapılabilir
                    showPenaltyDialog = true
                    isSheetOpen = false
                }) {
                    Text("Ceza Ekle")
                }
            }
        }
    }

    if (showScoreDialog) {
        ScorePenaltyDialog(
            title = "Skor Ekle",
            onDismiss = { showScoreDialog = false },
            onConfirm = { /* Burada skoru ekleyebilirsin */ }
        )
    }

    if (showPenaltyDialog) {
        ScorePenaltyDialog(
            title = "Ceza Ekle",
            onDismiss = { showPenaltyDialog = false },
            onConfirm = { /* Burada cezayı ekleyebilirsin */ }
        )
    }
}

@Composable
fun YazbozScreenContent(modifier: Modifier = Modifier) {
    val scoresList = listOf(50, 100, 35, 40) // Örnek skor listesi

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            text = "YAZBOZ",
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            repeat(4) { // 4 sütun için döngü
                Column(
                    modifier = Modifier
                        .weight(0.25f)
                        .fillMaxHeight()
                ) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(scoresList) { score ->
                            Text(
                                text = score.toString(),
                                fontSize = 18.sp,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                    Text(
                        text = "Toplam: ${scoresList.sum()}",
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                if (it < 3) VerticalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun YazbozScreenPreview() {
    YazbozScreen()
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
                        onValueChange = { newValue ->
                            inputValues = inputValues.toMutableList().also { it[index] = newValue }
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
