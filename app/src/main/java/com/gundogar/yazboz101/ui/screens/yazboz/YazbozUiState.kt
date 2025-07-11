package com.gundogar.yazboz101.ui.screens.yazboz

data class YazbozUiState(
    val scores: List<List<Int>> = emptyList(),
    val isSheetOpen: Boolean = false,
    val showScoreDialog: Boolean = false,
    val showPenaltyDialog: Boolean = false
)
