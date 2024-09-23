package com.example.data.model

import com.example.utils.Constants

enum class RoleModel {
    DIRECTOR, EMLOYEE
}

fun String.getRoleByString(): RoleModel {
    return when (this) {
        Constants.Role.DIRECTOR -> RoleModel.DIRECTOR
        else -> RoleModel.EMLOYEE
    }
}

fun RoleModel.getStringByRole(): String {
    return when (this) {
        RoleModel.DIRECTOR -> Constants.Role.DIRECTOR
        else -> Constants.Role.EMPLOYEE
    }
}