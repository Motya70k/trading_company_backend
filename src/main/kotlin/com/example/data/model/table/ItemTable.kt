package com.example.data.model.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object ItemTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 100).uniqueIndex()
    val quantity: Column<Int> = integer("quantity")
    val addedBy: Column<Int> = integer("addedBy").references(EmployeeTable.id)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}