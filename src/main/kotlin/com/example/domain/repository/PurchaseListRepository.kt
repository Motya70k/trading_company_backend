package com.example.domain.repository

import com.example.data.model.ItemModel
import com.example.data.model.PurchaseListModel

interface PurchaseListRepository {

    suspend fun addPurchase(purchase: PurchaseListModel)

    suspend fun getAllPurchase(): List<PurchaseListModel>

    suspend fun updatePurchase(purchase: PurchaseListModel)

    suspend fun deletePurchase(purchaseId: Int)

    suspend fun getPurchaseById(purchaseId: Int): PurchaseListModel?

    suspend fun fetchPurchaseByName(name: String): List<PurchaseListModel>
}