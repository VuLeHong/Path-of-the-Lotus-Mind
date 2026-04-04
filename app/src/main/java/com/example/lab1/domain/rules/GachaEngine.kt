package com.example.lab1.domain.rules

object GachaEngine {
    fun spin(realm: RealmConfig): OrbType {

        val pool = realm.gachaPool

        val totalWeight = pool.values.sum()

        var roll = (1..totalWeight).random()

        for ((orbType, weight) in pool) {

            roll -= weight

            if (roll <= 0) {
                return orbType
            }
        }

        return pool.keys.first()
    }
}