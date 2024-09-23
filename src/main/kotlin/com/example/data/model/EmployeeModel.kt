package com.example.data.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeModel(
    val id: Int,
    val login: String,
    val password: String,
    val name: String,
    val lastname: String,
    val phoneNumber: String,
    val role: RoleModel,
): Principal
