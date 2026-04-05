package com.example.lab1.domain.rules

import com.example.lab1.R

enum class OrbType(
    val displayName: String,
) {
    YELLOW("Yellow Orb"),
    GREEN("Green Orb"),
    BLUE("Blue Orb"),
    PURPLE("Purple Orb"),
    RED("Red Orb");
    companion object {
        fun fromName(name: String): OrbType? {
            return entries.find { it.name == name }
        }
        fun getOrbImage(orb: OrbType): Int {
            return when (orb) {
                YELLOW -> R.drawable.yellow_orb
                GREEN -> R.drawable.green_orb
                BLUE -> R.drawable.blue_orb
                PURPLE -> R.drawable.purple_orb
                RED -> R.drawable.purple_orb
            }
        }
    }
}