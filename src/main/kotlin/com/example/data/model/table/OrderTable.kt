package com.example.data.model.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object OrderTable : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val itemId: Column<Int> = integer("itemId").references(ItemTable.id)
    val itemName: Column<String> = varchar("itemName", 50)
    val clientId: Column<Int> = integer("clientId")
    val clientName: Column<String> = varchar("clientName", 50).references(ClientTable.name)
    val clientLastname: Column<String> = varchar("clientLastname", 50).references(ClientTable.lastname)
    val amount: Column<Int> = integer("amount")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}