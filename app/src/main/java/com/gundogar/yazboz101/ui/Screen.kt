package com.gundogar.yazboz101.ui

import com.gundogar.yazboz101.data.GameMode
import com.gundogar.yazboz101.data.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object MenuScreen : Screen()

    @Serializable
    data class YazbozScreen(
        val players: List<Player>,
        val gameMode: GameMode = GameMode.INDIVIDUAL,
        // 0 = yeni oyun; >0 ise mevcut bir oyun devam ettiriliyor demektir.
        val gameId: Int = 0
    ) : Screen()

    @Serializable
    object PreviousGamesScreen : Screen()

    @Serializable
    data class WinnerScreen(val players: List<Player>) : Screen()

}