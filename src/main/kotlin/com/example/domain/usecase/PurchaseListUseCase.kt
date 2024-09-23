package com.example.domain.usecase

import com.example.data.model.PurchaseListModel
import com.example.domain.repository.PurchaseListRepository

class PurchaseListUseCase(
    private val purchaseListRepository: PurchaseListRepository
) {
    suspend fun addPurchase(purchase: PurchaseListModel) {
        purchaseListRepository.addPurchase(purchase = purchase)
    }

    suspend fun getAllPurchase(): List<PurchaseListModel> {
        return purchaseListRepository.getAllPurchase()
    }

    suspend fun updatePurchase(purchase: PurchaseListModel) {
        purchaseListRepository.updatePurchase(purchase = purchase)
    }

    suspend fun deletePurchase(purchaseId: Int) {
        purchaseListRepository.deletePurchase(purchaseId = purchaseId)
    }

    suspend fun fetchPurchaseByName(name: String): List<PurchaseListModel> {
        return purchaseListRepository.fetchPurchaseByName(name)
    }

    suspend fun getPurchaseById(purchaseId: Int): PurchaseListModel? {
        return purchaseListRepository.getPurchaseById(purchaseId)
    }
}