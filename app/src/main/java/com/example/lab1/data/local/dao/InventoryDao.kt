package com.example.lab1.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab1.data.local.entity.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory")
    fun observeInventory(): Flow<List<InventoryItemEntity>>

    @Query("SELECT * FROM inventory WHERE orbTypeName = :typeName")
    suspend fun getItem(typeName: String): InventoryItemEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: InventoryItemEntity)

    @Query("""
        UPDATE inventory
        SET quantity = quantity + :amount
        WHERE orbTypeName = :typeName
    """)
    suspend fun addQuantity(typeName: String, amount: Int)

    @Query("""
        UPDATE inventory
        SET quantity = quantity - :amount
        WHERE orbTypeName = :typeName
        AND quantity >= :amount
    """)
    suspend fun consumeOrb(typeName: String, amount: Int)
}