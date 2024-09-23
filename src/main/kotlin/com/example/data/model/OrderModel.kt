package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderModel(
    val id: Int,
    val itemId: Int,
    val itemName: String,
    val clientId: Int,
    val clientName: String,
    val clientLastname: String,
    val amount: Int
)
