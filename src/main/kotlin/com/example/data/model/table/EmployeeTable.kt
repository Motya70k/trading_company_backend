package com.example.data.model.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object EmployeeTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val login: Column<String> = varchar("login", 50).uniqueIndex()
    val password: Column<String> = varchar("password", 200)
    val name: Column<String> = varchar("name", 50)
    val lastname: Column<String> = varchar("lastname", 50)
    val phoneNumber: Column<String> = varchar("phoneNumber", 12).uniqueIndex()
    val role: Column<String> = varchar("role", 50)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}