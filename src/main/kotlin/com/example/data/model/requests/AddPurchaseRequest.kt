package com.example.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddPurchaseRequest(
    val id: Int? = null,
    val itemId: Int,
    val amount: Int,
    val cost: Int
)