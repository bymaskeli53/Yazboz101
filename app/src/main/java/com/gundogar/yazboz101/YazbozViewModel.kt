package com.gundogar.yazboz101

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YazbozViewModel @Inject constructor(private val dao: YazbozDao) : ViewModel() {
    private val _uiState = MutableStateFlow(YazbozUiState())
    val uiState: StateFlow<YazbozUiState> = _uiState

    private fun saveGame(players: List<Player>) {
        viewModelScope.launch {
            try {
                dao.insertGame(YazbozItem(players = players))
                Log.e("YazbozViewModel", "Oyun kaydedildi: $players")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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

            is YazbozUiEvent.SaveGame -> saveGame(players = event.players)
        }

    }
}


