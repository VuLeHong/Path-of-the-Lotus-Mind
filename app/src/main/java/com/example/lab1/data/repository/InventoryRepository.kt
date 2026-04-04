package com.example.lab1.data.repository

import com.example.lab1.data.local.dao.InventoryDao
import com.example.lab1.data.local.entity.InventoryItemEntity
import com.example.lab1.domain.rules.OrbType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InventoryRepository @Inject constructor(
    private val dao: InventoryDao
) {
    fun observeInventory(): Flow<List<InventoryItemEntity>> {
        return dao.observeInventory()
    }

    suspend fun getQuantity(orbType: OrbType): Int {
        val item = dao.getItem(orbType.name)
        return item?.quantity ?: 0
    }

    suspend fun addOrb(orbType: OrbType, amount: Int = 1) {

        val item = dao.getItem(orbType.name)

        if (item == null) {
            dao.insert(
                InventoryItemEntity(
                    orbTypeName = orbType.name,
                    quantity = amount
                )
            )
        } else {
            dao.addQuantity(orbType.name, amount)
        }
    }

    suspend fun consumeOrb(orbType: OrbType, amount: Int) {
        dao.consumeOrb(orbType.name, amount)
    }
}