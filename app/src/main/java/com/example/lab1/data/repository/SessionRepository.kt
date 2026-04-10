package com.example.lab1.data.repository

import com.example.lab1.data.local.dao.SessionDao
import com.example.lab1.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val dao: SessionDao
) {
    fun observeAllSessions(): Flow<List<SessionEntity>> {
        return dao.observeAllSessions()
    }

    suspend fun saveSession(
        startTime: Long,
        endTime: Long,
        durationSeconds: Long,
        targetSeconds: Long,
        expChange: Int,
        isSuccess: Boolean,
        realmAtStart: Int
    ) {
        dao.insert(
            SessionEntity(
                startTime = startTime,
                endTime = endTime,
                durationSeconds = durationSeconds,
                targetSeconds = targetSeconds,
                expChange = expChange,
                isSuccess = isSuccess,
                realmAtStart = realmAtStart
            )
        )
    }

    suspend fun getRecentSessions(limit: Int = 20): List<SessionEntity> {
        return dao.getRecentSessions(limit)
    }

    suspend fun getFocusDays(): List<String> {
        return dao.getFocusDays()
    }

    suspend fun getYesterdayXp(): Int {
        return dao.getYesterdayXp() ?: 0
    }

    suspend fun getLast3DaysXp(): Int {
        return dao.getLast3DaysXp() ?: 0
    }
}