package com.example.lab1.domain.rules

enum class OrbType(
    val displayName: String,
    val colorHex: String
) {
    YELLOW("Hoàng Châu", "#FFD700"),
    GREEN("Lục Châu", "#00C853"),
    BLUE("Lam Châu", "#2979FF"),
    PURPLE("Tử Châu", "#AA00FF"),
    RED("Hồng Châu", "#DD2C00");
    companion object {
        fun fromName(name: String): OrbType? {
            return entries.find { it.name == name }
        }
    }
}