package com.example.lab1.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab1.data.local.dao.CharacterDao
import com.example.lab1.data.local.dao.InventoryDao
import com.example.lab1.data.local.dao.SessionDao
import com.example.lab1.data.local.entity.CharacterEntity
import com.example.lab1.data.local.entity.SessionEntity
import com.example.lab1.data.local.entity.InventoryItemEntity

@Database(
    entities = [
        CharacterEntity::class,
        SessionEntity::class,
        InventoryItemEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase: RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun sessionDao(): SessionDao

    abstract fun inventoryDao(): InventoryDao
}