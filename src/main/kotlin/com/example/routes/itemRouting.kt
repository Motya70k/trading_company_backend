package com.example.routes

import com.example.data.model.EmployeeModel
import com.example.data.model.ItemModel
import com.example.data.model.requests.AddItemRequest
import com.example.data.model.response.BaseResponse
import com.example.domain.usecase.ItemUseCase
import com.example.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.itemRouting(itemUseCase: ItemUseCase) {

    authenticate("jwt") {

        get("/get-all-items") {

            try {
                val item = itemUseCase.getAllItems()
                call.respond(HttpStatusCode.OK, item)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, BaseResponse(
                        false,
                        e.message ?: Constants.Error.GENERAL
                    )
                )
            }
        }

        post("/create-item") {
            val itemRequest = call.receiveNullable<AddItemRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val item = ItemModel(
                    id = 0,
                    name = itemRequest.name,
                    quantity = itemRequest.quantity,
                    addedBy = call.principal<EmployeeModel>()!!.id
                )

                val existingItemName = itemUseCase.fetchItemByName(item.name)
                if (existingItemName != null) {
                    call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.ITEM_EXISTS))
                }

                itemUseCase.addItem(item = item)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.ITEM_ADDED_SUCCESSFULLY))

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, e.message ?: BaseResponse(
                        false,
                        Constants.Error.GENERAL
                    )
                )
            }
        }

        post("/update-item") {
            val itemRequest = call.receiveNullable<AddItemRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val ownerId = call.principal<EmployeeModel>()!!.id
                val item = ItemModel(
                    id = itemRequest.id ?: 0,
                    name = itemRequest.name,
                    quantity = itemRequest.quantity,
                    addedBy = ownerId
                )

                val existingItem = itemUseCase.getItemById(item.id)
                val existingItemName = itemUseCase.getItemByName(item.name)

                if (existingItem == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.ITEM_NOT_FOUND))
                    return@post
                }

                if (existingItemName != null && existingItemName.id != item.id) {
                    call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.ITEM_EXISTS))
                }
                itemUseCase.updateItem(item = item, ownerId = ownerId)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.ITEM_UPDATE_SUCCESSFULLY))

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, e.message ?: BaseResponse(
                        false,
                        Constants.Error.GENERAL
                    )
                )
            }
        }

        delete("/delete-item") {
            val itemRequest = call.request.queryParameters[Constants.Value.ID]?.toInt() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }

            try {
                val ownerId = call.principal<EmployeeModel>()!!.id
                val existingItem = itemUseCase.getItemById(itemRequest)

                if (existingItem == null) {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.ITEM_NOT_FOUND))
                    return@delete
                }

                itemUseCase.deleteItem(itemId = itemRequest, ownerId = ownerId)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.ITEM_DELETE_SUCCESSFULLY))

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, e.message ?: BaseResponse(
                        false,
                        Constants.Error.GENERAL
                    )
                )
            }
        }

        get("/search-item") {
            val name = call.request.queryParameters["name"]

            if (name.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@get
            }

            try {
                val item = itemUseCase.fetchItemByName(name)
                if (item != null) {
                    call.respond(HttpStatusCode.OK, item)
                } else {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.ITEM_NOT_FOUND))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }
    }
}