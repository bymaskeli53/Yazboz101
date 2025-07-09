package com.gundogar.yazboz101

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviousGamesViewModel @Inject constructor(
    private val dao: YazbozDao
) : ViewModel() {

    private val _games = MutableStateFlow<List<YazbozItem>>(emptyList())
    val games: StateFlow<List<YazbozItem>> = _games

    init {
        getGames()
    }

    private fun getGames() {
        viewModelScope.launch {
            dao.getAllGames().collect { items ->
                _games.value = items
            }
        }
    }
}
