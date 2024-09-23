package com.example.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddClientRequest(
    val id: Int? = null,
    val name: String,
    val lastname: String,
    val phone: String
)
