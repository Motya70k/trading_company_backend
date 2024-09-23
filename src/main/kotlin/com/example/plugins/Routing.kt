package com.example.plugins

import com.example.domain.usecase.*
import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    employeeUseCase: EmployeeUseCase,
    itemUseCase: ItemUseCase,
    purchaseListUseCase: PurchaseListUseCase,
    clientUseCase: ClientUseCase,
    orderUseCase: OrderUseCase
) {

    routing {
        employeeRoute(employeeUseCase = employeeUseCase)
        itemRouting(itemUseCase = itemUseCase)
        purchaseListRouting(purchaseListUseCase = purchaseListUseCase, itemUseCase = itemUseCase)
        clientRouting(clientUseCase = clientUseCase)
        orderRouting(orderUseCase = orderUseCase, itemUseCase = itemUseCase, clientUseCase = clientUseCase)
    }
}
