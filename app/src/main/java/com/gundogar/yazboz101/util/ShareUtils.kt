package com.gundogar.yazboz101.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.gundogar.yazboz101.data.Player
import java.io.File

fun shareImageWithText(context: Context) {
    val bitmap = createImageWithText("Kolay maçtı")
    val uri = saveImageToCache(context, bitmap) ?: return

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_TEXT, "Kolay maçtı!!!")
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

/**
 * Oyun sonucunu gösteren paylaşılabilir bir skor kartı görseli üretir.
 * 101'de en düşük toplam kazandığı için oyuncular puana göre artan sıralanır.
 */
fun generateResultCardBitmap(players: List<Player>): Bitmap {
    val sorted = players.sortedBy { it.scores.sum() }
    val width = 1080
    val height = 600 + (sorted.size * 80)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val centerX = width / 2f
    val gold = Color.parseColor("#D4B100")

    // Arka plan degrade: koyu mor -> pembe
    val gradientPaint = Paint().apply {
        shader = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            intArrayOf(Color.parseColor("#1D2671"), Color.parseColor("#C33764")),
            null, Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), gradientPaint)

    // Başlık
    val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = WHITE
        textSize = 60f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("🏆 YAZBOZ 101 SONUÇLARI", centerX, 110f, titlePaint)

    // Kazanan vurgusu
    val winner = sorted.first()
    val winnerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = gold
        textSize = 72f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("${winner.name} Kazandı!", centerX, 240f, winnerPaint)

    // Altın ayraç çizgisi
    val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = gold
        strokeWidth = 4f
    }
    canvas.drawLine(80f, 310f, width - 80f, 310f, dividerPaint)

    // Oyuncu satırları
    val rowBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#22FFFFFF")
    }
    val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 48f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.LEFT
    }
    val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = WHITE
        textSize = 48f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.RIGHT
    }

    val rowHeight = 80f
    var rowTop = 370f
    sorted.forEachIndexed { index, player ->
        val rect = RectF(80f, rowTop, width - 80f, rowTop + rowHeight - 16f)
        canvas.drawRoundRect(rect, 24f, 24f, rowBackgroundPaint)

        val medal = when (index) {
            0 -> "🥇"
            1 -> "🥈"
            2 -> "🥉"
            else -> "${index + 1}."
        }
        namePaint.color = if (index == 0) gold else WHITE
        val baseline = rect.centerY() + namePaint.textSize / 3f
        canvas.drawText("$medal ${player.name}", 110f, baseline, namePaint)
        canvas.drawText("${player.scores.sum()} puan", width - 110f, baseline, scorePaint)

        rowTop += rowHeight
    }

    // Alt bilgi
    val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = WHITE
        alpha = 128
        textSize = 32f
        textAlign = Paint.Align.RIGHT
    }
    canvas.drawText("Yazboz101 ile oynandı", width - 40f, height - 40f, footerPaint)

    return bitmap
}

fun shareResultCard(context: Context, players: List<Player>) {
    val bitmap = generateResultCardBitmap(players)
    val uri = saveImageToCache(context, bitmap) ?: return

    val sorted = players.sortedBy { it.scores.sum() }
    val winner = sorted.first()
    val shareText = buildString {
        appendLine("🏆 ${winner.name} kazandı!")
        appendLine()
        sorted.forEachIndexed { index, player ->
            val medal = when (index) {
                0 -> "🥇"
                1 -> "🥈"
                2 -> "🥉"
                else -> "${index + 1}."
            }
            appendLine("$medal ${player.name}: ${player.scores.sum()} puan")
        }
        appendLine()
        append("Yazboz101 ile oynandı")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_TEXT, shareText)
        setPackage("com.whatsapp")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "WhatsApp yüklü değil!", Toast.LENGTH_SHORT).show()
    }
}