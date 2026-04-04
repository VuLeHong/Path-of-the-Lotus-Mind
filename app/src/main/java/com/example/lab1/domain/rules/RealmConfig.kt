package com.example.lab1.domain.rules

import com.example.lab1.domain.rules.RealmConfig.QI_CONDENSATION
import com.example.lab1.domain.rules.RealmConfig.entries

enum class RealmConfig(
    val realmName: String,
    val xpRequired: Long,
    val orbRequirements: List<OrbRequirement>,
    val gachaPool: Map<OrbType, Int>
) {
    QI_CONDENSATION(
        realmName = "QI_CONDENSATION",
        xpRequired = 0,
        orbRequirements = emptyList(),
        gachaPool = mapOf(
            OrbType.YELLOW to 100
        )
    ),

    FOUNDATION_BUILDING(
        realmName = "FOUNDATION_BUILDING",
        xpRequired = 3000,
            orbRequirements = listOf(
                OrbRequirement(OrbType.YELLOW, 5)
            ),
        gachaPool = mapOf(
            OrbType.YELLOW to 70,
            OrbType.GREEN to 30
        )
    ),

    CORE_FORMATION(
        realmName = "CORE_FORMATION",
        xpRequired = 9000,
        orbRequirements = listOf(
            OrbRequirement(OrbType.YELLOW, 7),
            OrbRequirement(OrbType.GREEN, 5)
        ),
        gachaPool = mapOf(
            OrbType.YELLOW to 50,
            OrbType.GREEN to 30,
            OrbType.BLUE to 20
        )
    ),

    NASCENT_SOUL(
        realmName = "NASCENT_SOUL",
        xpRequired = 16200,
        orbRequirements = listOf(
            OrbRequirement(OrbType.YELLOW, 4),
            OrbRequirement(OrbType.GREEN, 5),
            OrbRequirement(OrbType.BLUE, 5)
        ),
        gachaPool = mapOf(
            OrbType.YELLOW to 25,
            OrbType.GREEN to 25,
            OrbType.BLUE to 30,
            OrbType.PURPLE to 20
        )
    ),

    SPIRIT_TRANSFORMATION(
        realmName = "SPIRIT_TRANSFORMATION",
        xpRequired = 28200,
        orbRequirements = listOf(
            OrbRequirement(OrbType.GREEN, 6),
            OrbRequirement(OrbType.BLUE, 6),
            OrbRequirement(OrbType.PURPLE, 6)
        ),
        gachaPool = mapOf(
            OrbType.YELLOW to 20,
            OrbType.GREEN to 20,
            OrbType.BLUE to 20,
            OrbType.PURPLE to 20,
            OrbType.RED to 20
        )
    );

    companion object {

        fun fromOrdinal(ordinal: Int): RealmConfig {
            return entries.getOrElse(ordinal) { QI_CONDENSATION }
        }

        fun next(current: RealmConfig): RealmConfig? {
            return entries.getOrNull(current.ordinal + 1)
        }

        fun previous(current: RealmConfig): RealmConfig? {
            return entries.getOrNull(current.ordinal - 1)
        }
        fun fromExp(exp: Long): RealmConfig {
            return entries.lastOrNull { exp >= it.xpRequired }
                ?: QI_CONDENSATION
        }
    }

}
