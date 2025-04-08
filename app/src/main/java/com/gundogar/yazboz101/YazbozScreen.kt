package com.gundogar.yazboz101

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.gundogar.yazboz101.ui.theme.LightGrayishPaper
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YazbozScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var showScoreDialog by remember { mutableStateOf(false) }
    var showPenaltyDialog by remember { mutableStateOf(false) }

    val scores = remember { mutableStateListOf<MutableList<Int>>() }

    Scaffold(
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            YazbozScreenContent(
                modifier = Modifier.fillMaxSize(),
                scores = scores
            )

            ExtendedFloatingActionButton(
                onClick = { isSheetOpen = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(72.dp)
                    .height(72.dp)
                    .padding(8.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Ekle")
            }

            Icon(
                Icons.Filled.Share,
                modifier = Modifier.align(Alignment.TopStart).width(60.dp).height(60.dp).clickable { shareImageWithText(context) },
                tint = androidx.compose.ui.graphics.Color.Black,
                contentDescription = null
            )
        }
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
                    showScoreDialog = true
                    isSheetOpen = false
                }) {
                    Text("Skor Ekle")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
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
            onConfirm = { newScores ->
                scores.add(newScores.toMutableStateList())
            }
        )
    }

    if (showPenaltyDialog) {
        ScorePenaltyDialog(
            title = "Ceza Ekle",
            onDismiss = { showPenaltyDialog = false },
            onConfirm = { penalties ->
                scores.add(penalties.map { -it }.toMutableStateList())
            }
        )
    }
}

@Composable
fun YazbozScreenContent(modifier: Modifier = Modifier, scores: List<List<Int>>) {
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
                    Text(text = "Babalar", color = androidx.compose.ui.graphics.Color.Black)
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



fun shareImageWithText(context: Context) {
    val bitmap = createImageWithText("Babalar Ezdi")
    val uri = saveImageToCache(context, bitmap) ?: return

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_TEXT, "Babalar Ezdi! 🎉")
        setPackage("com.whatsapp") // Sadece WhatsApp'a yönlendirir
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "WhatsApp yüklü değil!", Toast.LENGTH_SHORT).show()
    }
}

fun createImageWithText(text: String): Bitmap {
    val width = 500
    val height = 250
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        color = AndroidColor.BLACK
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }
    canvas.drawColor(AndroidColor.WHITE) // Arka plan rengi
    canvas.drawText(text, width / 2f, height / 2f, paint)
    return bitmap
}

fun saveImageToCache(context: Context, bitmap: Bitmap): Uri? {
    val file = File(context.cacheDir, "share_image.png")
    return try {
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



