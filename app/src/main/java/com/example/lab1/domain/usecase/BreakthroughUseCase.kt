package com.example.lab1.domain.usecase

import com.example.lab1.data.repository.CharacterRepository
import com.example.lab1.data.repository.InventoryRepository
import com.example.lab1.domain.rules.BreakthroughInfo
import com.example.lab1.domain.rules.OrbType
import com.example.lab1.domain.rules.RealmConfig
import javax.inject.Inject

class BreakthroughUseCase @Inject constructor(
    private val inventoryRepo: InventoryRepository,
    private val characterRepo: CharacterRepository
) {
    suspend fun getBreakthroughInfo(): BreakthroughInfo? {

        val character = characterRepo.getCharacter() ?: return null
        val currentRealm = RealmConfig.fromOrdinal(character.realmOrdinal)

        val nextRealm = RealmConfig.next(currentRealm)
            ?: return null

        val missingXp = (nextRealm.xpRequired - character.exp)
            .coerceAtLeast(0)

        val missingOrbs = mutableListOf<Pair<OrbType, Int>>()

        for (req in nextRealm.orbRequirements) {

            val have = inventoryRepo.getQuantity(req.orbType)

            if (have < req.quantity) {
                missingOrbs.add(req.orbType to (req.quantity - have))
            }
        }

        val canBreak = missingXp == 0L && missingOrbs.isEmpty()

        return BreakthroughInfo(
            targetRealm = nextRealm,
            canBreak = canBreak,
            missingXp = missingXp,
            missingOrbs = missingOrbs
        )
    }

    suspend fun breakthrough(): Boolean {

        val character = characterRepo.getCharacter() ?: return false
        val currentRealm = RealmConfig.fromOrdinal(character.realmOrdinal)

        val nextRealm = RealmConfig.next(currentRealm)
            ?: return false

        val info = getBreakthroughInfo() ?: return false

        if (!info.canBreak) return false

        for (req in nextRealm.orbRequirements) {
            inventoryRepo.consumeOrb(req.orbType, req.quantity)
        }

        characterRepo.updateExpAndRealm(
            exp = character.exp,
            realmOrdinal = nextRealm.ordinal
        )
        return true
    }
}