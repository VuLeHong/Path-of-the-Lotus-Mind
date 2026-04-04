package com.example.lab1.domain.usecase

import com.example.lab1.data.repository.CharacterRepository
import com.example.lab1.data.repository.InventoryRepository
import com.example.lab1.data.repository.SessionRepository
import com.example.lab1.domain.rules.GachaEngine
import com.example.lab1.domain.rules.OrbType
import com.example.lab1.domain.rules.PenaltyEngine
import com.example.lab1.domain.rules.RealmConfig
import com.example.lab1.domain.rules.SessionResult
import javax.inject.Inject

class CompleteSessionUseCase @Inject constructor(
    private val characterRepo: CharacterRepository,
    private val sessionRepo: SessionRepository,
    private val inventoryRepo: InventoryRepository
) {

    suspend fun invoke(
        startTime: Long,
        targetSeconds: Long,
        elapsedSeconds: Long,
        isSuccess: Boolean
    ): SessionResult {

        val character = characterRepo.getCharacter()!!
        val currentRealm = RealmConfig.fromOrdinal(character.realmOrdinal)

        val sessionXp = (elapsedSeconds / 60).toInt()

        var droppedOrbs: List<OrbType> = emptyList()
        var penaltyDesc: String? = null

        val (newExp, newRealmOrdinal) = if (isSuccess) {

            val updatedExp = character.exp + sessionXp

            if (elapsedSeconds >= 900) {

                val orbCount = (2..5).random()
                val orbs = mutableListOf<OrbType>()

                repeat(orbCount) {
                    val orb = GachaEngine.spin(currentRealm)
                    inventoryRepo.addOrb(orb)
                    orbs.add(orb)
                }

                droppedOrbs = orbs
            }

            updatedExp to character.realmOrdinal

        } else {

            val past1DayXp = sessionRepo.getYesterdayXp()
            val past3DaysXp = sessionRepo.getLast3DaysXp()

            val result = PenaltyEngine.applyPenalty(
                currentExp = character.exp,
                currentRealm = currentRealm,
                sessionXp = sessionXp.toLong(),
                past1DayXp = past1DayXp.toLong(),
                past3DaysXp = past3DaysXp.toLong()
            )

            penaltyDesc = result.description

            result.newExp to result.newRealmOrdinal
        }

        val expGained = (newExp - character.exp)
            .toInt()
            .coerceAtLeast(0)

        sessionRepo.saveSession(
            startTime = startTime,
            endTime = System.currentTimeMillis(),
            durationSeconds = elapsedSeconds,
            targetSeconds = targetSeconds,
            expGained = expGained,
            isSuccess = isSuccess,
            realmAtStart = character.realmOrdinal
        )

        characterRepo.updateExpAndRealm(
            exp = newExp,
            realmOrdinal = newRealmOrdinal
        )

        val leveledUp = false

        return SessionResult(
            expGained = expGained,
            newExp = newExp,
            newRealmOrdinal = newRealmOrdinal,
            leveledUp = leveledUp,
            penaltyDescription = penaltyDesc,
            droppedOrbs = droppedOrbs
        )
    }
}