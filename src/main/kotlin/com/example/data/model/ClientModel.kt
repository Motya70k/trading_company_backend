package com.example.data.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class ClientModel(
    val id: Int,
    val name: String,
    val lastname: String,
    val phone: String
): Principal
