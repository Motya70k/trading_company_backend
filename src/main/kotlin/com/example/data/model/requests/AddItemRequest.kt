package com.example.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddItemRequest(
    val id: Int? = null,
    val name: String,
    val quantity: Int
)
