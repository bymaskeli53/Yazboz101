package com.gundogar.yazboz101.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gundogar.yazboz101.data.GameMode
import com.gundogar.yazboz101.data.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerNameDialog(
    onDismiss: () -> Unit,
    onConfirm: (players: List<Player>, gameMode: GameMode) -> Unit
) {
    val context = LocalContext.current

    var gameMode by remember { mutableStateOf(GameMode.INDIVIDUAL) }
    // İki mod için isimler ayrı tutulur, böylece sekmeler arası geçişte girilenler kaybolmaz.
    val individualNames = remember { mutableStateListOf("", "", "", "") }
    val teamNames = remember { mutableStateListOf("", "") }

    val names = if (gameMode == GameMode.TEAM) teamNames else individualNames
    val isTeam = gameMode == GameMode.TEAM

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFFFFBF0),
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                // Başlık
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Color(0xFFD4B100),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isTeam) "Takım İsimlerini Girin" else "Oyuncu İsimlerini Girin",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D2D2D)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Oyun modu seçimi
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = !isTeam,
                        onClick = { gameMode = GameMode.INDIVIDUAL },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color(0xFFD4B100),
                            activeContentColor = Color.White
                        )
                    ) {
                        Text("Bireysel")
                    }
                    SegmentedButton(
                        selected = isTeam,
                        onClick = { gameMode = GameMode.TEAM },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color(0xFFD4B100),
                            activeContentColor = Color.White
                        )
                    ) {
                        Text("Takım")
                    }

                }

                Spacer(modifier = Modifier.height(20.dp))

                // İsim alanları
                names.forEachIndexed { index, value ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { names[index] = it },
                        label = {
                            Text(
                                if (isTeam) "Takım ${index + 1}" else "Oyuncu ${index + 1}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD4B100),
                            focusedLabelColor = Color(0xFFD4B100),
                            cursorColor = Color(0xFFD4B100),
                            unfocusedBorderColor = Color(0xFFBBBBBB),
                            unfocusedLabelColor = Color(0xFF777777)
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Butonlar
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFBBBBBB)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF666666)
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            "İptal",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    Button(
                        onClick = {
                            if (names.all { it.isNotBlank() }) {
                                val players = names.map {
                                    Player(name = it.trim(), scores = listOf(0, 0, 0, 0))
                                }
                                onConfirm(players, gameMode)
                                onDismiss()
                            } else {
                                Toast.makeText(
                                    context,
                                    if (isTeam) "Lütfen tüm takım isimlerini girin."
                                    else "Lütfen tüm oyuncu isimlerini girin.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD4B100),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(48.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Başla",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
