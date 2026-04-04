package com.example.lab1.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory")
data class InventoryItemEntity(
    @PrimaryKey
    val orbTypeName: String,
    val quantity: Int = 0
)
