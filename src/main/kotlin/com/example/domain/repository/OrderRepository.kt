package com.example.domain.repository

import com.example.data.model.ClientModel
import com.example.data.model.OrderModel
import com.example.data.model.table.OrderTable

interface OrderRepository {

    suspend fun addOrder(order: OrderModel)

    suspend fun getAllOrders(): List<OrderModel>

    suspend fun updateOrder(order: OrderModel)

    suspend fun deleteOrder(id: Int)

    suspend fun fetchOrderByClientNameAndClientLastname(name: String, lastname: String): List<OrderModel>

    suspend fun getOrderById(orderId: Int): OrderModel?
}