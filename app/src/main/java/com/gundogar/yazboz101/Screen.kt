package com.gundogar.yazboz101

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object MenuScreen : Screen()

    @Serializable
    object YazbozScreen : Screen()

}