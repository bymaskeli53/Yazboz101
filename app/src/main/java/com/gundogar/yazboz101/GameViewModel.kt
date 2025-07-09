package com.gundogar.yazboz101

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val dao: YazbozDao) : ViewModel() {

    fun saveGame(players: List<Player>) {
        viewModelScope.launch {
            dao.insertGame(YazbozItem(players = players))
            Log.d("GameViewModel", "Oyun kaydedildi: $players")
        }
    }
}