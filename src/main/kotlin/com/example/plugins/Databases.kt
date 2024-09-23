package com.example.plugins

import com.example.data.model.table.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory{

    private val dbUrl = "jdbc:mysql://localhost:3307/trading_company_db"
    private val dbUser = "root"
    private val dbPassword = ""

    fun Application.initializeDataBase() {
        Database.connect(getHikariDataSource())

        transaction {
            SchemaUtils.create(
                EmployeeTable, ItemTable, PurchaseListTable, ClientTable, OrderTable
            )
        }
    }

    private fun getHikariDataSource(): HikariDataSource {

        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = "com.mysql.cj.jdbc.Driver"
        hikariConfig.jdbcUrl = dbUrl
        hikariConfig.username = dbUser
        hikariConfig.password = dbPassword
        hikariConfig.maximumPoolSize = 3
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block: () -> T): T {
        return withContext(Dispatchers.IO) {
            transaction { block() }
        }
    }
}
