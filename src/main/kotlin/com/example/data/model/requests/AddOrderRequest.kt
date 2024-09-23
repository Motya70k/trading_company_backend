package com.example.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddOrderRequest(
    val id: Int? = null,
    val itemId: Int,
    val clientName: String,
    val clientLastname: String,
    val amount: Int
)
