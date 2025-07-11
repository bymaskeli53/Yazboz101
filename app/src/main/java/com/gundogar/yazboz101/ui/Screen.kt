package com.gundogar.yazboz101.ui

import com.gundogar.yazboz101.data.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object MenuScreen : Screen()

    @Serializable
    data class YazbozScreen(val players: List<Player>) : Screen()

    @Serializable
    object PreviousGamesScreen : Screen()


}