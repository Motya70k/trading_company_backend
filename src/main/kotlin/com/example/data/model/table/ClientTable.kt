package com.example.data.model.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object ClientTable : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50).uniqueIndex()
    val lastname: Column<String> = varchar("surname", 50).uniqueIndex()
    val phone: Column<String> = varchar("phone", 12)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}