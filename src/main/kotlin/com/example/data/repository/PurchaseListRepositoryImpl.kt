package com.example.data.repository

import com.example.data.model.PurchaseListModel
import com.example.data.model.table.ItemTable

import com.example.data.model.table.PurchaseListTable
import com.example.data.model.table.PurchaseListTable.id
import com.example.domain.repository.PurchaseListRepository
import com.example.plugins.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PurchaseListRepositoryImpl : PurchaseListRepository {
    override suspend fun addPurchase(purchase: PurchaseListModel) {
        val itemName = dbQuery {
            ItemTable.selectAll().where { ItemTable.id eq purchase.itemId }
                .map { it[ItemTable.name] }
                .singleOrNull()
        }
        dbQuery {
            PurchaseListTable.insert { table ->
                table[itemId] = purchase.itemId
                table[name] = itemName!!
                table[amount] = purchase.amount
                table[cost] = purchase.cost
            }
        }
    }

    override suspend fun getAllPurchase(): List<PurchaseListModel> {
        return dbQuery {
            PurchaseListTable.selectAll()
                .mapNotNull { rowToPurchase(it) }
        }
    }

    override suspend fun updatePurchase(purchase: PurchaseListModel) {
        val itemName = dbQuery {
            ItemTable.selectAll().where { ItemTable.id eq purchase.itemId }
                .map { it[ItemTable.name] }
                .singleOrNull()
        }
        dbQuery {
            PurchaseListTable.update(
                where = {
                    PurchaseListTable.id.eq(purchase.id)
                }
            ) { table ->
                table[itemId] = purchase.itemId
                table[name] = itemName!!
                table[amount] = purchase.amount
                table[cost] = purchase.cost
            }
        }
    }

    override suspend fun deletePurchase(purchaseId: Int) {
        dbQuery {
            PurchaseListTable.deleteWhere { id.eq(purchaseId) }
        }
    }

    override suspend fun getPurchaseById(purchaseId: Int): PurchaseListModel? {
        return dbQuery {
            PurchaseListTable.selectAll().where(id.eq(purchaseId))
                .mapNotNull { rowToPurchase(it) }
                .singleOrNull()
        }
    }

    override suspend fun fetchPurchaseByName(name: String): List<PurchaseListModel> {
        return dbQuery {
            PurchaseListTable.selectAll().where(PurchaseListTable.name.eq(name))
                .mapNotNull { rowToPurchase(it) }
        }
    }

    private fun rowToPurchase(row: ResultRow): PurchaseListModel? {
        if (row == null) {
            return null
        }

        return PurchaseListModel(
            id = row[PurchaseListTable.id],
            itemId = row[PurchaseListTable.itemId],
            name = row[PurchaseListTable.name],
            amount = row[PurchaseListTable.amount],
            cost = row[PurchaseListTable.cost]
        )
    }
}