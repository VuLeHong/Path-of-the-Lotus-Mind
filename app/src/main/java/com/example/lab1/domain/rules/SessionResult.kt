package com.example.lab1.domain.rules

data class SessionResult(
    val expChange: Int,
    val newExp: Long,
    val newRealmOrdinal: Int,
    val leveledUp: Boolean,
    val penaltyDescription: String?,
    val droppedOrbs: List<OrbType>
)
