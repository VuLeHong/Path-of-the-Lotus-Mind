package com.example.lab1.domain.rules

data class BreakthroughInfo(
    val targetRealm: RealmConfig,
    val canBreak: Boolean,
    val missingXp: Long,
    val missingOrbs: List<Pair<OrbType, Int>>
)
