package com.gundogar.yazboz101

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

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
        color = BLACK
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }
    canvas.drawColor(WHITE) // Arka plan rengi
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