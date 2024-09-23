package com.example.data.repository

import com.example.data.model.ItemModel
import com.example.data.model.table.ItemTable
import com.example.data.model.table.ItemTable.id
import com.example.domain.repository.ItemRepository
import com.example.plugins.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ItemRepositoryImpl : ItemRepository {

    override suspend fun addItem(item: ItemModel) {
        dbQuery {
            ItemTable.insert { table ->
                table[name] = item.name
                table[quantity] = item.quantity
                table[addedBy] = item.addedBy
            }
        }
    }

    override suspend fun getAllItems(): List<ItemModel> {
        return dbQuery {
            ItemTable.selectAll()
                .mapNotNull { rowToItem(it) }
        }
    }

    override suspend fun updateItem(item: ItemModel, ownerId: Int) {
        dbQuery {
            ItemTable.update(
                where = {
                    ItemTable.addedBy.eq(ownerId) and (ItemTable.id.eq(item.id))
                }
            ) { table ->
                table[name] = item.name
                table[quantity] = item.quantity
                table[addedBy] = ownerId
            }
        }
    }

    override suspend fun deleteItem(itemId: Int, ownerId: Int) {
        dbQuery {
            ItemTable.deleteWhere { id.eq(itemId) and addedBy.eq(ownerId) }
        }
    }

    override suspend fun checkItemExists(itemId: Int): Boolean {
        val item = getItemById(itemId)
        return item != null
    }

    override suspend fun getItemById(itemId: Int): ItemModel? {
        return dbQuery {
            ItemTable.selectAll().where(id.eq(itemId))
                .mapNotNull { rowToItem(it) }
                .singleOrNull()
        }
    }

    override suspend fun fetchItemByName(itemName: String): List<ItemModel> {
        return dbQuery {
            ItemTable.selectAll().where(ItemTable.name.eq(itemName))
                .mapNotNull { rowToItem(it) }
        }
    }

    override suspend fun getItemByName(itemName: String): ItemModel? {
        return dbQuery {
            ItemTable.selectAll().where(ItemTable.name.eq(itemName))
                .mapNotNull { rowToItem(it) }
                .singleOrNull()
        }
    }

    override suspend fun updateItemQuantity(itemId: Int, newQuantity: Int) {
        dbQuery {
            ItemTable.update(
                where = { ItemTable.id eq itemId }
            ) {
                it[quantity] = newQuantity
            }
        }
    }

    private fun rowToItem(row: ResultRow): ItemModel? {
        if (row == null) {
            return null
        }

        return ItemModel(
            id = row[ItemTable.id],
            name = row[ItemTable.name],
            quantity = row[ItemTable.quantity],
            addedBy = row[ItemTable.addedBy]
        )
    }
}