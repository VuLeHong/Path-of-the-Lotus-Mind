package com.example.lab1.data.repository

import com.example.lab1.data.local.dao.CharacterDao
import com.example.lab1.data.local.entity.CharacterEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CharacterRepository @Inject constructor(
    private val dao: CharacterDao
) {
    fun observeCharacter(): Flow<CharacterEntity?> {
        return dao.observeCharacter()
    }

    suspend fun getCharacter(): CharacterEntity? {
        return dao.getCharacter()
    }

    suspend fun createCharacter(name: String) {
        dao.upsert(
            CharacterEntity(
                username = name
            )
        )
    }

    suspend fun updateExpAndRealm(exp: Long, realmOrdinal: Int) {
        dao.updateExpAndRealm(exp, realmOrdinal)
    }

    suspend fun setMeditating(value: Boolean) {
        dao.setMeditating(value)
    }

    suspend fun hasCharacter(): Boolean {
        return dao.getCharacter() != null
    }
}