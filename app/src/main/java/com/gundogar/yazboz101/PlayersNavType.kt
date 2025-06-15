package com.gundogar.yazboz101

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

val PlayersNavType = object : NavType<Oyuncular>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Oyuncular? {
        val jsonString = bundle.getString(key) ?: return null
        return try {
            Json.decodeFromString<Oyuncular>(jsonString)
        } catch (e: Exception) {
            null
        }
    }

    override fun parseValue(value: String): Oyuncular {
        return try {
            val decodedValue = URLDecoder.decode(value, "UTF-8")
            Json.decodeFromString<Oyuncular>(decodedValue)
        } catch (e: Exception) {
            Oyuncular(emptyList())
        }
    }

    override fun serializeAsValue(value: Oyuncular): String {
        return try {
            val jsonString = Json.encodeToString(value)
            URLEncoder.encode(jsonString, "UTF-8")
        } catch (e: Exception) {
            ""
        }
    }

    override fun put(bundle: Bundle, key: String, value: Oyuncular) {
        val jsonString = Json.encodeToString(value)
        bundle.putString(key, jsonString)
    }
}