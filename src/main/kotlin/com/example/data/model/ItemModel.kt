package com.example.data.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class ItemModel(
    val id: Int,
    val name: String,
    val quantity: Int,
    val addedBy: Int
): Principal
