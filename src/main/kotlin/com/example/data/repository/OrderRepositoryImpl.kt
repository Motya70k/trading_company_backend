package com.example.data.repository

import com.example.data.model.ClientModel
import com.example.data.model.OrderModel
import com.example.data.model.table.ClientTable
import com.example.data.model.table.ItemTable
import com.example.data.model.table.OrderTable
import com.example.domain.repository.OrderRepository
import com.example.domain.usecase.ItemUseCase
import com.example.plugins.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class OrderRepositoryImpl(
    private val itemRepositoryImpl: ItemRepositoryImpl
) : OrderRepository {
    override suspend fun addOrder(order: OrderModel) {
        val selectedItemName = getItemName(order)
        val selectedClientId = getClientId(order)

        val currentItem = itemRepositoryImpl.getItemById(order.itemId)
        val currentQuantity = currentItem?.quantity ?: 0
        dbQuery {
            OrderTable.insert { table ->
                table[itemId] = order.itemId
                table[itemName] = selectedItemName!!
                table[clientId] = selectedClientId!!
                table[clientName] = order.clientName
                table[clientLastname] = order.clientLastname
                table[amount] = order.amount
            }
        }

        val newQuantity = currentQuantity - order.amount
        if (currentItem != null) {
            itemRepositoryImpl.updateItemQuantity(order.itemId, newQuantity)
        }
    }

    override suspend fun getAllOrders(): List<OrderModel> {
        return dbQuery {
            OrderTable.selectAll()
                .mapNotNull { rowToOrder(it) }
        }
    }

    override suspend fun updateOrder(order: OrderModel) {

        val selectedItemName = getItemName(order)
        val selectedClientId = getClientId(order)

        val amountDifference = calculateAmountDifference(order)

        dbQuery {
            OrderTable.update(
                where = {
                    OrderTable.id.eq(order.id)
                }
            ) { table ->
                table[itemId] = order.itemId
                table[itemName] = selectedItemName!!
                table[clientId] = selectedClientId!!
                table[clientName] = order.clientName
                table[clientLastname] = order.clientLastname
                table[amount] = order.amount
            }
        }
        val currentItem = itemRepositoryImpl.getItemById(order.itemId)
        val currentQuantity = currentItem?.quantity ?: 0

        if (currentItem != null) {
            val newQuantity = currentQuantity - amountDifference
            itemRepositoryImpl.updateItemQuantity(order.itemId, newQuantity)
        }
    }

    override suspend fun deleteOrder(id: Int) {
        dbQuery {
            OrderTable.deleteWhere { OrderTable.id.eq(id) }
        }
    }

    override suspend fun fetchOrderByClientNameAndClientLastname(name: String, lastname: String): List<OrderModel> {
        return dbQuery {
            OrderTable.selectAll().where(
                OrderTable.clientName.eq(name) and
                        (OrderTable.clientLastname.eq(lastname))
            )
                .mapNotNull { rowToOrder(it) }
        }
    }

    override suspend fun getOrderById(orderId: Int): OrderModel? {
        return dbQuery {
            OrderTable.selectAll().where(OrderTable.id.eq(orderId))
                .mapNotNull { rowToOrder(it) }
                .singleOrNull()
        }
    }

    private fun rowToOrder(row: ResultRow): OrderModel? {
        if (row == null) {
            return null
        }

        return OrderModel(
            id = row[OrderTable.id],
            itemId = row[OrderTable.itemId],
            itemName = row[OrderTable.itemName],
            clientId = row[OrderTable.clientId],
            clientName = row[OrderTable.clientName],
            clientLastname = row[OrderTable.clientLastname],
            amount = row[OrderTable.amount]
        )
    }

    private suspend fun getItemName(order: OrderModel): String? {
        return dbQuery {
            ItemTable.selectAll().where { ItemTable.id eq order.itemId }
                .map { it[ItemTable.name] }
                .singleOrNull()
        }
    }

    private suspend fun getClientId(order: OrderModel): Int? {
        return dbQuery {
            ClientTable.selectAll().where {
                ClientTable.name eq order.clientName and
                        (ClientTable.lastname eq order.clientLastname)
            }
                .map { it[ClientTable.id] }
                .singleOrNull()
        }
    }

    private suspend fun calculateAmountDifference(order: OrderModel): Int {
        val oldOrder = getOrderById(order.id) ?: return 0
        return order.amount - oldOrder.amount
    }
}