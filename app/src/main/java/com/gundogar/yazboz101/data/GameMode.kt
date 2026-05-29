package com.gundogar.yazboz101.data

import kotlinx.serialization.Serializable

/**
 * Bir oyunun nasıl oynandığını belirtir.
 *
 * [INDIVIDUAL] her oyuncu kendi adına oynar (4 oyuncu / 4 sütun).
 * [TEAM] ikişer kişilik takımlar halinde oynanır (2 takım / 2 sütun). Takım modunda
 * her sütun bir takımı temsil eder ve [Player.name] takım adını tutar.
 */
@Serializable
enum class GameMode {
    INDIVIDUAL,
    TEAM
}
