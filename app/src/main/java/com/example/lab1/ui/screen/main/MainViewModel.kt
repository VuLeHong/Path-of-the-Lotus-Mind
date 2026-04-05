package com.example.lab1.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab1.data.repository.CharacterRepository
import com.example.lab1.domain.rules.OrbType
import com.example.lab1.domain.rules.RealmConfig
import com.example.lab1.domain.usecase.CompleteSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExpUi(
    val current: Long,
    val required: Long,
    val progress: Float
)

data class RewardUi(
    val exp: Int,
    val orbs: Map<OrbType, Int>,
    val isSuccess: Boolean,
    val penalty: String?
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepo: CharacterRepository,
    private val completeSessionUseCase: CompleteSessionUseCase
) : ViewModel() {

    val character = characterRepo
        .observeCharacter()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _sessionDuration = MutableStateFlow(0L)
    val sessionDuration: StateFlow<Long> = _sessionDuration

    private val _startTime = MutableStateFlow(0L)
    val startTime: StateFlow<Long> = _startTime

    private val _reward = MutableStateFlow<RewardUi?>(null)

    val reward: StateFlow<RewardUi?> = _reward

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

        ExpUi(current, required, progress)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpUi(0,1,0f))

    fun startSession(durationSeconds: Long) {
        viewModelScope.launch {
            _startTime.value = System.currentTimeMillis()
            _sessionDuration.value = durationSeconds
            characterRepo.setMeditating(true)
        }
    }

    fun completeSession(
        startTime: Long,
        targetSeconds: Long,
        elapsedSeconds: Long,
        isSuccess: Boolean
    ) {
        viewModelScope.launch {

            val result = completeSessionUseCase.invoke(
                startTime = startTime,
                targetSeconds = targetSeconds,
                elapsedSeconds = elapsedSeconds,
                isSuccess = isSuccess
            )

            val orbMap = result.droppedOrbs
                .groupingBy { it }
                .eachCount()

            _reward.value = RewardUi(
                exp = result.expGained,
                orbs = orbMap,
                isSuccess = result.penaltyDescription == null,
                penalty = result.penaltyDescription
            )

            characterRepo.setMeditating(false)
        }
    }
    fun clearReward() {
        _reward.value = null
    }

}