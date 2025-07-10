package com.gundogar.yazboz101

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