package com.example.lab1.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lab1.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: SessionEntity): Long

    @Query("SELECT * FROM session ORDER BY startTime DESC")
    fun observeAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM session ORDER BY startTime DESC LIMIT :limit")
    suspend fun getRecentSessions(limit: Int = 20): List<SessionEntity>

    @Query("""
        SELECT DISTINCT date(startTime / 1000, 'unixepoch')
        FROM session
        WHERE isSuccess = 1
        ORDER BY startTime DESC
        """)
    suspend fun getFocusDays(): List<String>

    @Query("""    
    SELECT SUM(expChange)
    FROM session
    WHERE isSuccess = 1
    AND date(startTime / 1000, 'unixepoch') = date('now', '-1 day')
""")
    suspend fun getYesterdayXp(): Int?

    @Query("""
    SELECT SUM(expChange)
    FROM session
    WHERE isSuccess = 1
    AND date(startTime / 1000, 'unixepoch')
    BETWEEN date('now','-3 day') AND date('now','-1 day')
""")
    suspend fun getLast3DaysXp(): Int?
}