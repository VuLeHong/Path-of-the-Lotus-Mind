package com.example.lab1.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab1.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM character WHERE id = 1")
    fun observeCharacter(): Flow<CharacterEntity?>

    @Query("SELECT * FROM character WHERE id = 1")
    suspend fun getCharacter(): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(character: CharacterEntity)

    @Query("UPDATE character SET exp = :exp, realmOrdinal = :realm WHERE id = 1")
    suspend fun updateExpAndRealm(exp: Long, realm: Int)

    @Query("UPDATE character SET isMeditating = :meditating WHERE id = 1")
    suspend fun setMeditating(meditating: Boolean)
}