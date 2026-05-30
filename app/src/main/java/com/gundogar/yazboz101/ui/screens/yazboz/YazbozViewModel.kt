package com.gundogar.yazboz101.ui.screens.yazboz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gundogar.yazboz101.data.GameMode
import com.gundogar.yazboz101.data.Player
import com.gundogar.yazboz101.data.YazbozDao
import com.gundogar.yazboz101.data.YazbozItem
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

    // Kayıtlı bir oyunu sürdürürken skorların yalnızca bir kez yüklenmesini sağlar
    // (örn. ekran döndürmede tekrar yüklenip eklenen elleri silmemesi için).
    private var isLoaded = false

    /**
     * Kayıtlı oyuncuların skorlarını (her oyuncu için el bazlı liste) ekranın
     * kullandığı el bazlı yapıya (her el için oyuncu bazlı liste) dönüştürür.
     */
    private fun loadGame(players: List<Player>) {
        if (isLoaded) return
        isLoaded = true
        val roundCount = players.maxOfOrNull { it.scores.size } ?: 0
        if (roundCount == 0) return
        val rounds = (0 until roundCount).map { round ->
            players.map { it.scores.getOrNull(round) ?: 0 }
        }
        _uiState.update { it.copy(scores = rounds) }
    }

    private fun saveGame(players: List<Player>, gameMode: GameMode, gameId: Int) {
        viewModelScope.launch {
            try {
                // gameId > 0 ise REPLACE ile aynı kayıt güncellenir (kopya oluşmaz).
                dao.insertGame(YazbozItem(players = players, gameMode = gameMode, id = gameId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onEvent(event: YazbozUiEvent) {
        when (event) {
            is YazbozUiEvent.OpenSheet -> _uiState.update { it.copy(isSheetOpen = true) }
            is YazbozUiEvent.CloseSheet -> _uiState.update {
                it.copy(
                    isSheetOpen = false,
                    showScoreDialog = false,
                    showPenaltyDialog = false
                )
            }

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

            YazbozUiEvent.Share -> {
            }

            is YazbozUiEvent.ShowFinishDialog -> _uiState.update {
                it.copy(showFinishDialog = true)
            }

            is YazbozUiEvent.CloseFinishDialog -> _uiState.update {
                it.copy(showFinishDialog = false)
            }

            is YazbozUiEvent.LoadGame -> loadGame(event.players)

            is YazbozUiEvent.SaveGame -> saveGame(
                players = event.players,
                gameMode = event.gameMode,
                gameId = event.gameId
            )
        }

    }
}


