package com.example.lab1.ui.screen.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import  com.example.lab1.data.repository.CharacterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val characterRepo: CharacterRepository
) : ViewModel() {

    val hasCharacter: StateFlow<Boolean?> = flow {
        emit(characterRepo.getCharacter() != null)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun createCharacter(name: String, onDone: () -> Unit) {
        viewModelScope.launch {
            if (name.isBlank()) return@launch
            characterRepo.createCharacter(name.trim())
            onDone()
        }
    }
}