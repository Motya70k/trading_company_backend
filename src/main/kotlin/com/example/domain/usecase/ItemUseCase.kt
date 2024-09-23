package com.example.domain.usecase

import com.example.data.model.ClientModel
import com.example.data.model.ItemModel
import com.example.domain.repository.ItemRepository

class ItemUseCase(
    private val itemRepository: ItemRepository
) {

    suspend fun addItem(item: ItemModel) {
        itemRepository.addItem(item = item)
    }

    suspend fun getAllItems(): List<ItemModel> {
        return itemRepository.getAllItems()
    }

    suspend fun updateItem(item: ItemModel, ownerId: Int) {
        itemRepository.updateItem(item = item, ownerId = ownerId)
    }

    suspend fun deleteItem(itemId: Int, ownerId: Int) {
        itemRepository.deleteItem(itemId = itemId, ownerId = ownerId)
    }

    suspend fun checkItemExist(itemId: Int): Boolean {
        return itemRepository.checkItemExists(itemId)
    }

    suspend fun getItemById(itemId: Int): ItemModel? {
        return itemRepository.getItemById(itemId)
    }

    suspend fun fetchItemByName(itemName: String): List<ItemModel> {
        return itemRepository.fetchItemByName(itemName)
    }

    suspend fun getItemByName(itemName: String): ItemModel? {
        return itemRepository.getItemByName(itemName)
    }

    suspend fun updateItemQuantity(itemId: Int, newQuantity: Int) {
        itemRepository.updateItemQuantity(itemId, newQuantity)
    }
}