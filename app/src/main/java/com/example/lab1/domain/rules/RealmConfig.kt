package com.example.lab1.domain.rules

import com.example.lab1.R

enum class RealmConfig(
    val realmName: String,
    val xpRequired: Long,
    val orbRequirements: List<OrbRequirement>,
    val gachaPool: Map<OrbType, Int>,
    val auraSymbol: Int,
    val auraName: String,
    val description: String = "",
    val auraCircle: Int
) {
    QI_CONDENSATION(
        realmName = "QI_CONDENSATION",
        xpRequired = 0,
        orbRequirements = emptyList(),
        gachaPool = mapOf(
            OrbType.YELLOW to 100
        ),
        auraSymbol = 0,
        auraName = "",
        description = "The cultivator begins by gathering and stabilizing spiritual energy within the body, forming the foundation of all future growth.",
        auraCircle = 0
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
        ),
        auraSymbol = R.drawable.leaf_aura,
        auraName = "leaf_aura",
        description = "Spiritual energy is refined into a stable base, allowing the body to withstand and control greater power.",
        auraCircle = R.drawable.aura_1
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
        ),
        auraSymbol = R.drawable.blossom_aura,
        auraName = "blossom_aura",
        description = "Energy is compressed into a core, marking the shift from accumulation to controlled transformation.",
        auraCircle = R.drawable.aura_2
    ),

    NASCENT_SOUL(
        realmName = "NASCENT_SOUL",
        xpRequired = 16200,
        orbRequirements = listOf(
            OrbRequirement(OrbType.PURPLE, 4),
            OrbRequirement(OrbType.BLUE, 5),
            OrbRequirement(OrbType.GREEN, 5)
        ),
        gachaPool = mapOf(
            OrbType.YELLOW to 25,
            OrbType.GREEN to 25,
            OrbType.BLUE to 30,
            OrbType.PURPLE to 20
        ),
        auraSymbol = R.drawable.cosmo_aura,
        auraName = "cosmo_aura",
        description = "A spiritual consciousness is formed, enabling the cultivator to act beyond the limits of the physical body.",
        auraCircle = R.drawable.aura_3
    ),

    SPIRIT_TRANSFORMATION(
        realmName = "SPIRIT_TRANSFORMATION",
        xpRequired = 28200,
        orbRequirements = listOf(
            OrbRequirement(OrbType.RED, 4),
            OrbRequirement(OrbType.BLUE, 10),
            OrbRequirement(OrbType.PURPLE, 6)
        ),
        gachaPool = mapOf(
            OrbType.YELLOW to 20,
            OrbType.GREEN to 20,
            OrbType.BLUE to 20,
            OrbType.PURPLE to 20,
            OrbType.RED to 20
        ),
        auraSymbol = R.drawable.prosperity_aura,
        auraName = "prosperity_aura",
        description = "The body aligns with the nature of spiritual energy, granting control at a higher level and nearing transcendence.",
        auraCircle = R.drawable.aura_4
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
        fun Map<OrbType, Int>.toRatePercent(): Map<OrbType, Int> {
            val total = values.sum().toFloat()
            return mapValues { (_, v) ->
                ((v / total) * 100).toInt()
            }
        }
    }

}
