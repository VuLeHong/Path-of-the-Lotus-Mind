package com.example.lab1.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab1.data.repository.CharacterRepository
import com.example.lab1.domain.rules.RealmConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExpUi(
    val current: Long,
    val required: Long,
    val progress: Float
)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepo: CharacterRepository
) : ViewModel() {

    val character = characterRepo
        .observeCharacter()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val expUi: StateFlow<ExpUi> = character.map { char ->
        char ?: return@map ExpUi(0, 1, 0f)

        val currentRealm = RealmConfig.fromOrdinal(char.realmOrdinal)
        val nextRealm = RealmConfig.next(currentRealm)

        val prevXp = currentRealm.xpRequired
        val nextXp = nextRealm?.xpRequired ?: currentRealm.xpRequired

        val current = char.exp - prevXp
        val required = (nextXp - prevXp).coerceAtLeast(1L)

        val progress = (current.toFloat() / required)
            .coerceIn(0f, 1f)

        ExpUi(
            current = current,
            required = required,
            progress = progress
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpUi(0,1,0f))

    fun startSession(durationSeconds: Long) {
        viewModelScope.launch {
            characterRepo.setMeditating(true)
        }
    }

    fun stopSession() {
        viewModelScope.launch {
            characterRepo.setMeditating(false)
        }
    }
}