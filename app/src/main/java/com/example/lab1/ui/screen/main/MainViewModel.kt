package com.example.lab1.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab1.data.local.entity.SessionEntity
import com.example.lab1.data.repository.CharacterRepository
import com.example.lab1.data.repository.InventoryRepository
import com.example.lab1.data.repository.SessionRepository
import com.example.lab1.domain.rules.BreakthroughInfo
import com.example.lab1.domain.rules.OrbType
import com.example.lab1.domain.rules.RealmConfig
import com.example.lab1.domain.usecase.BreakthroughUseCase
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

data class HistoryItemUi(
    val isSuccess: Boolean,
    val durationText: String,
    val dateText: String,
    val expText: String
)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepo: CharacterRepository,
    private val completeSessionUseCase: CompleteSessionUseCase,
    private val inventoryRepo: InventoryRepository,
    private val sessionRepository: SessionRepository,
    private val breakthroughUseCase: BreakthroughUseCase
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
    private val _history = MutableStateFlow<List<HistoryItemUi>>(emptyList())
    val history: StateFlow<List<HistoryItemUi>> = _history
    private val _breakthroughInfo = MutableStateFlow<BreakthroughInfo?>(null)
    val breakthroughInfo: StateFlow<BreakthroughInfo?> = _breakthroughInfo

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

    val inventory: StateFlow<Map<OrbType, Int>> =
        flow {
            val map = mutableMapOf<OrbType, Int>()

            OrbType.entries.forEach { orb ->
                map[orb] = inventoryRepo.getQuantity(orb)
            }

            emit(map)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap()
        )
    fun startSession(durationSeconds: Long) {
        viewModelScope.launch {
            if (_sessionDuration.value > 0) return@launch
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
                exp = result.expChange,
                orbs = orbMap,
                isSuccess = result.penaltyDescription == null,
                penalty = result.penaltyDescription
            )

            characterRepo.setMeditating(false)
            _sessionDuration.value = 0L
            _startTime.value = 0L
        }
    }
    fun clearReward() {
        _reward.value = null
        viewModelScope.launch {
            _breakthroughInfo.value = breakthroughUseCase.getBreakthroughInfo()
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            val sessions = sessionRepository.getRecentSessions()

            _history.value = sessions.map { session ->
                mapToUi(session)
            }
        }
    }
    private fun mapToUi(session: SessionEntity): HistoryItemUi {

        val minutes = session.durationSeconds / 60
        val seconds = session.durationSeconds % 60

        val durationText =
            if (minutes > 0) "${minutes}m ${seconds}s"
            else "${seconds}s"

        val date = java.text.SimpleDateFormat(
            "dd/MM HH:mm",
            java.util.Locale.getDefault()
        ).format(java.util.Date(session.startTime))

        val expText = if(session.isSuccess) "+ ${session.expChange} XP" else " ${session.expChange} XP"

        return HistoryItemUi(
            isSuccess = session.isSuccess,
            durationText = durationText,
            dateText = date,
            expText = expText
        )
    }

    fun loadBreakthrough() {
        if (_breakthroughInfo.value != null) return
        viewModelScope.launch {
            _breakthroughInfo.value = breakthroughUseCase.getBreakthroughInfo()
        }
    }

    fun doBreakthrough() {
        viewModelScope.launch {
            val success = breakthroughUseCase.breakthrough()

            if (success) {
                _breakthroughInfo.value = breakthroughUseCase.getBreakthroughInfo()
            }
        }
    }

}