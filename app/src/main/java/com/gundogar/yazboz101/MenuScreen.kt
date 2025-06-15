package com.gundogar.yazboz101

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreen(onNavigateToYazboz: (String, String, String, String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val isimler = remember { mutableStateListOf("", "", "", "") }
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Oyuncu İsimlerini Girin") },
            text = {
                Column {
                    isimler.forEachIndexed { index, value ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = { isimler[index] = it },
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
                    if (isimler.all { it.isNotBlank() }) {
                        onNavigateToYazboz(isimler[0], isimler[1], isimler[2], isimler[3])
                        showDialog = false
                    } else {
                        Toast.makeText(
                            context,
                            "Lütfen tüm oyuncu isimlerini girin.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    Text("Başla")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_main_yazboz),
            contentDescription = "Oyun Logosu",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.weight(0.5f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            MenuButton(text = "Yeni Oyun") { showDialog = true }


            MenuButton(text = "Önceki Oyunlarım")  { }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
