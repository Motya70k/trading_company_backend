package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseListModel(
    val id: Int,
    val itemId: Int,
    val name: String,
    val amount: Int,
    val cost: Int
)