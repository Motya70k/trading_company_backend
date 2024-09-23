package com.example.data.model.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val name: String,
    val lastname: String,
    val phoneNumber: String,
    val role: String
)
