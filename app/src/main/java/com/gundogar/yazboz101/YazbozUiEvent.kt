package com.gundogar.yazboz101

sealed class YazbozUiEvent {
    object OpenSheet : YazbozUiEvent()
    object CloseSheet : YazbozUiEvent()
    object ShowScoreDialog : YazbozUiEvent()
    object ShowPenaltyDialog : YazbozUiEvent()
    data class AddScores(val scores: List<Int>) : YazbozUiEvent()
    data class AddPenalties(val penalties: List<Int>) : YazbozUiEvent()
    object Share : YazbozUiEvent()
}
