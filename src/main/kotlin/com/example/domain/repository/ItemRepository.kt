package com.example.domain.repository

import com.example.data.model.ItemModel

interface ItemRepository {

    suspend fun addItem(item: ItemModel)

    suspend fun getAllItems(): List<ItemModel>

    suspend fun updateItem(item: ItemModel, ownerId: Int)

    suspend fun deleteItem(itemId: Int, ownerId: Int)

    suspend fun checkItemExists(itemId: Int): Boolean

    suspend fun getItemById(itemId: Int): ItemModel?

    suspend fun fetchItemByName(itemName: String): List<ItemModel>

    suspend fun getItemByName(itemName: String): ItemModel?

    suspend fun updateItemQuantity(itemId: Int, newQuantity: Int)
}