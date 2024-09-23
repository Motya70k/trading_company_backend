package com.example.data.model.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PurchaseListTable : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val itemId: Column<Int> = integer("itemId").references(ItemTable.id)
    val name: Column<String> = varchar("name", 100)
    val amount: Column<Int> = integer("amount")
    val cost: Column<Int> = integer("cost")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}