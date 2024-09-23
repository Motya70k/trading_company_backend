package com.example.data.repository

import com.example.data.model.ClientModel
import com.example.data.model.table.ClientTable
import com.example.domain.repository.ClientRepository
import com.example.plugins.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ClientRepositoryImpl : ClientRepository {
    override suspend fun addClient(client: ClientModel) {
        dbQuery {
            ClientTable.insert { table ->
                table[name] = client.name
                table[lastname] = client.lastname
                table[phone] = client.phone
            }
        }
    }

    override suspend fun updateClient(client: ClientModel) {
        dbQuery {
            ClientTable.update(
                where = { ClientTable.id.eq(client.id) }
            ) { table ->
                table[name] = client.name
                table[lastname] = client.lastname
                table[phone] = client.phone
            }
        }
    }

    override suspend fun deleteClient(id: Int) {
        dbQuery {
            ClientTable.deleteWhere { ClientTable.id.eq(id) }
        }
    }

    override suspend fun getAllClients(): List<ClientModel> {
        return dbQuery {
            ClientTable.selectAll()
                .mapNotNull { rowToClient(it) }
        }
    }

    override suspend fun checkClientExist(name: String, lastname: String): Boolean {
        val client = getClientByNameAndLastname(name, lastname)
        return client != null
    }

    override suspend fun getClientByNameAndLastname(name: String, lastname: String): List<ClientModel> {
        return dbQuery {
            ClientTable.selectAll().where(ClientTable.name.eq(name) and (ClientTable.lastname.eq(lastname)))
                .mapNotNull { rowToClient(it) }
        }
    }

    private fun rowToClient(row: ResultRow): ClientModel? {
        if (row == null) {
            return null
        }

        return ClientModel(
            id = row[ClientTable.id],
            name = row[ClientTable.name],
            lastname = row[ClientTable.lastname],
            phone = row[ClientTable.phone],
        )
    }
}