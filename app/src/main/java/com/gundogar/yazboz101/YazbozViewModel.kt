package com.gundogar.yazboz101

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class YazbozViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(YazbozUiState())
    val uiState: StateFlow<YazbozUiState> = _uiState

    fun onEvent(event: YazbozUiEvent) {
        when (event) {
            is YazbozUiEvent.OpenSheet -> _uiState.update { it.copy(isSheetOpen = true) }
            is YazbozUiEvent.CloseSheet -> _uiState.update { it.copy(isSheetOpen = false) }
            is YazbozUiEvent.ShowScoreDialog -> _uiState.update {
                it.copy(showScoreDialog = true, isSheetOpen = false)
            }
            is YazbozUiEvent.ShowPenaltyDialog -> _uiState.update {
                it.copy(showPenaltyDialog = true, isSheetOpen = false)
            }
            is YazbozUiEvent.AddScores -> _uiState.update {
                it.copy(
                    scores = it.scores + listOf(event.scores),
                    showScoreDialog = false
                )
            }
            is YazbozUiEvent.AddPenalties -> _uiState.update {
                val penalties = event.penalties.map { -it }
                it.copy(scores = it.scores + listOf(penalties), showPenaltyDialog = false)
            }
            YazbozUiEvent.Share -> { /* handled in View via callback */ }
        }
    }
}
