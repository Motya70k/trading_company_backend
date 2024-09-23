package com.example.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePurchaseRequest(
    val id: Int? = null,
    val amount: Int,
    val cost: Int
)