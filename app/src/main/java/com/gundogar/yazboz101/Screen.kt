package com.gundogar.yazboz101

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object MenuScreen : Screen()

    @Serializable
    data class YazbozScreen(val oyuncu1: String,val oyuncu2: String,val oyuncu3: String,val oyuncu4: String) : Screen()

}