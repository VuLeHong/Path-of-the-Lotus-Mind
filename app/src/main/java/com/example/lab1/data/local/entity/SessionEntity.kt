package com.example.lab1.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val durationSeconds: Long,
    val targetSeconds: Long,
    val expGained: Int,
    val isSuccess: Boolean,
    val realmAtStart: Int
)
