package com.example

import com.example.authentification.JwtService
import com.example.data.repository.*
import com.example.domain.usecase.*
import com.example.plugins.*
import com.example.plugins.DatabaseFactory.initializeDataBase
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 3007, host = "192.168.1.47", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val jwtService = JwtService()

    val employeeRepository = EmployeeRepositoryImpl()
    val itemRepository = ItemRepositoryImpl()
    val purchaseListRepository = PurchaseListRepositoryImpl()
    val clientRepository = ClientRepositoryImpl()
    val orderRepository = OrderRepositoryImpl(itemRepository)

    val employeeUseCase = EmployeeUseCase(employeeRepository, jwtService)
    val itemUseCase = ItemUseCase(itemRepository)
    val purchaseListUseCase = PurchaseListUseCase(purchaseListRepository)
    val clientUseCase = ClientUseCase(clientRepository)
    val orderUseCase = OrderUseCase(orderRepository)

    initializeDataBase()
    configureSerialization()
    configureMonitoring()
    configureSecurity(employeeUseCase)
    configureRouting(employeeUseCase, itemUseCase, purchaseListUseCase, clientUseCase, orderUseCase)
}
