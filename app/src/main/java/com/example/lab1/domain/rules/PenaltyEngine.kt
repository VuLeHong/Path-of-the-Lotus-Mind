package com.example.lab1.domain.rules

object PenaltyEngine {

    fun applyPenalty(
        currentExp: Long,
        currentRealm: RealmConfig,
        sessionXp: Long,
        past1DayXp: Long,
        past3DaysXp: Long
    ): PenaltyResult {

        return when (currentRealm) {
            RealmConfig.QI_CONDENSATION -> {

                val roll = (1..100).random()
                val loss = when {
                    roll <= 50 -> (sessionXp * 0.33).toLong()
                    roll <= 85 -> (sessionXp * 0.5).toLong()
                    else -> sessionXp
                }

                val newExp = (currentExp - loss).coerceAtLeast(0)

                PenaltyResult(
                    newExp = newExp,
                    newRealmOrdinal = currentRealm.ordinal,
                    description = "Thất bại — mất $loss XP"
                )
            }

            RealmConfig.FOUNDATION_BUILDING -> {

                val roll = (1..100).random()

                val loss = when {

                    roll <= 50 -> (sessionXp * 0.33).toLong()

                    roll <= 70 -> (sessionXp * 0.5).toLong()

                    roll <= 90 -> sessionXp

                    else -> sessionXp + past1DayXp   // session + past 1 day (demo)
                }

                val newExp = (currentExp - loss).coerceAtLeast(0)
                val realmFromExp = RealmConfig.fromExp(newExp)

                val newRealm = if (realmFromExp.ordinal < currentRealm.ordinal) {
                    realmFromExp
                } else {
                    currentRealm
                }

                PenaltyResult(
                    newExp = newExp,
                    newRealmOrdinal = newRealm.ordinal,
                    description = "Thất bại — mất $loss XP"
                )
            }

            RealmConfig.CORE_FORMATION -> {

                val roll = (1..100).random()

                val loss = when {

                    roll <= 20 -> (sessionXp * 0.33).toLong()

                    roll <= 50 -> (sessionXp * 0.5).toLong()

                    roll <= 90 -> sessionXp

                    else -> sessionXp + past1DayXp
                }

                val newExp = (currentExp - loss).coerceAtLeast(0)
                val realmFromExp = RealmConfig.fromExp(newExp)

                val newRealm = if (realmFromExp.ordinal < currentRealm.ordinal) {
                    realmFromExp
                } else {
                    currentRealm
                }

                PenaltyResult(
                    newExp = newExp,
                    newRealmOrdinal = newRealm.ordinal,
                    description = "Thất bại — mất $loss XP"
                )
            }

            RealmConfig.NASCENT_SOUL -> {

                val roll = (1..100).random()

                val loss = when {

                    roll <= 20 -> (sessionXp * 0.5).toLong()

                    roll <= 65 -> sessionXp

                    else -> sessionXp + past1DayXp
                }

                val newExp = (currentExp - loss).coerceAtLeast(0)
                val realmFromExp = RealmConfig.fromExp(newExp)

                val newRealm = if (realmFromExp.ordinal < currentRealm.ordinal) {
                    realmFromExp
                } else {
                    currentRealm
                }

                PenaltyResult(
                    newExp = newExp,
                    newRealmOrdinal = newRealm.ordinal,
                    description = "Thất bại — mất $loss XP"
                )
            }

            RealmConfig.SPIRIT_TRANSFORMATION -> {

                val roll = (1..100).random()

                val loss = when {

                    roll <= 50 -> sessionXp

                    roll <= 85 -> sessionXp + past1DayXp

                    else -> sessionXp + past3DaysXp
                }

                val newExp = (currentExp - loss).coerceAtLeast(0)
                val realmFromExp = RealmConfig.fromExp(newExp)

                val newRealm = if (realmFromExp.ordinal < currentRealm.ordinal) {
                    realmFromExp
                } else {
                    currentRealm
                }

                PenaltyResult(
                    newExp = newExp,
                    newRealmOrdinal = newRealm.ordinal,
                    description = "Thất bại — mất $loss XP"
                )
            }
        }
    }

}
