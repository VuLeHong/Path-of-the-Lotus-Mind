package com.example.lab1.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character")
data class CharacterEntity(
    @PrimaryKey
    val id: Int = 1,
    val username: String,
    val exp: Long = 0,
    val realmOrdinal: Int = 0,
    val isMeditating: Boolean = false
)
