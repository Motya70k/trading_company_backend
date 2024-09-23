package com.example.routes

import com.example.data.model.OrderModel
import com.example.data.model.requests.AddOrderRequest
import com.example.data.model.response.BaseResponse
import com.example.domain.usecase.ClientUseCase
import com.example.domain.usecase.ItemUseCase
import com.example.domain.usecase.OrderUseCase
import com.example.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.orderRouting(orderUseCase: OrderUseCase, itemUseCase: ItemUseCase, clientUseCase: ClientUseCase) {

    authenticate("jwt") {
        get("/get-all-orders") {
            try {
                val purchase = orderUseCase.getAllOrders()
                call.respond(HttpStatusCode.OK, purchase)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, BaseResponse(
                        false,
                        e.message ?: Constants.Error.GENERAL
                    )
                )
            }
        }

        post("/create-order") {
            val orderRequest = call.receiveNullable<AddOrderRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val itemExists = itemUseCase.checkItemExist(orderRequest.itemId)
                if (!itemExists) {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEMID_DOESNT_EXIST))
                    return@post
                }

                val clientExist = clientUseCase.checkClientExist(orderRequest.clientName, orderRequest.clientLastname)
                if (!clientExist) {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.CLIENT_DOESNT_EXIST))
                    return@post
                }

                val item = itemUseCase.getItemById(orderRequest.itemId)
                if (item != null) {
                    val quantity = item.quantity
                    if (quantity <= 0) {
                        call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEM_QUANTITY_ZERO_OR_NEGATIVE))
                        return@post
                    }

                    if (orderRequest.amount > quantity) {
                        call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEM_AMOUNT_GREATER_THAN_QUANTITY))
                        return@post
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEM_NOT_FOUND))
                    return@post
                }

                if (orderRequest.amount < 1) {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEM_AMOUNT_NULL_OR_MINUS))
                    return@post
                }

                val order = OrderModel(
                    id = 0,
                    itemId = orderRequest.itemId,
                    itemName = "",
                    clientId = 0,
                    clientName = orderRequest.clientName,
                    clientLastname = orderRequest.clientLastname,
                    amount = orderRequest.amount
                )

                orderUseCase.addOrder(order = order)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.ORDER_ADDED_SUCCESSFULLY))

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, e.message ?: BaseResponse(
                        false,
                        Constants.Error.GENERAL
                    )
                )
            }
        }

        post("/update-order") {
            val orderRequest = call.receiveNullable<AddOrderRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val itemExists = itemUseCase.checkItemExist(orderRequest.itemId)
                if (!itemExists) {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEMID_DOESNT_EXIST))
                    return@post
                }

                val clientExist = clientUseCase.checkClientExist(orderRequest.clientName, orderRequest.clientLastname)
                if (!clientExist) {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.CLIENT_DOESNT_EXIST))
                    return@post
                }

                val item = itemUseCase.getItemById(orderRequest.itemId)
                if (item != null) {
                    val quantityOnStock = item.quantity
                    if (quantityOnStock <= 0) {
                        call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEM_QUANTITY_ZERO_OR_NEGATIVE))
                        return@post
                    }

                    // Получаем текущий заказ
                    val existingOrder = orderUseCase.getOrderById(orderRequest.id ?: 0)
                    if (existingOrder == null) {
                        call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.ORDER_NOT_FOUND))
                        return@post
                    }

                    // Рассчитываем разницу между старым и новым количеством товара в заказе
                    val amountDifference = orderRequest.amount - existingOrder.amount

                    if (amountDifference > quantityOnStock) {
                        call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEM_AMOUNT_GREATER_THAN_QUANTITY))
                        return@post
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEM_NOT_FOUND))
                    return@post
                }

                if (orderRequest.amount < 1) {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.ITEM_AMOUNT_NULL_OR_MINUS))
                    return@post
                }

                val order = OrderModel(
                    id = orderRequest.id ?: 0,
                    itemId = orderRequest.itemId,
                    itemName = "",
                    clientId = 0,
                    clientName = orderRequest.clientName,
                    clientLastname = orderRequest.clientLastname,
                    amount = orderRequest.amount
                )

                val orderIdExist = orderUseCase.getOrderById(order.id)
                if (orderIdExist == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.ORDER_NOT_FOUND))
                    return@post
                }

                orderUseCase.updateOrder(order = order)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.ORDER_UPDATED_SUCCESSFULLY))

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, e.message ?: BaseResponse(
                        false,
                        Constants.Error.GENERAL
                    )
                )
            }
        }

        delete("/delete-order") {
            val orderRequest = call.request.queryParameters[Constants.Value.ID]?.toInt() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }

            try {
                val orderIdExist = orderUseCase.getOrderById(orderRequest)
                if (orderIdExist == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.ORDER_NOT_FOUND))
                    return@delete
                }
                orderUseCase.deleteOrder(id = orderRequest)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.ORDER_DELETED_SUCCESSFULLY))

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, e.message ?: BaseResponse(
                        false,
                        Constants.Error.GENERAL
                    )
                )
            }
        }

        get("/search-order") {
            val name = call.request.queryParameters["name"]
            val lastname = call.request.queryParameters["lastname"]

            if (name.isNullOrBlank() || lastname.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@get
            }

            try {
                val order = orderUseCase.fetchOrderByClientNameAndClientLastname(name, lastname)
                if (order != null) {
                    call.respond(HttpStatusCode.OK, order)
                } else {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.ORDER_NOT_FOUND))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }
    }
}