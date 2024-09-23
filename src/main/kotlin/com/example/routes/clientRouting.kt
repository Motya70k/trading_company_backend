package com.example.routes

import com.example.data.model.ClientModel
import com.example.data.model.requests.AddClientRequest
import com.example.data.model.response.BaseResponse
import com.example.domain.usecase.ClientUseCase
import com.example.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clientRouting(clientUseCase: ClientUseCase) {

    authenticate("jwt") {
        get("/get-all-clients") {
            try {
                val client = clientUseCase.getAllClients()
                call.respond(HttpStatusCode.OK, client)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, BaseResponse(
                        false,
                        e.message ?: Constants.Error.GENERAL
                    )
                )
            }
        }

        post("/add-client") {
            val clientRequest = call.receiveNullable<AddClientRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val client = ClientModel(
                    id = 0,
                    name = clientRequest.name,
                    lastname = clientRequest.lastname,
                    phone = clientRequest.phone
                )

                clientUseCase.addClient(client = client)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.CLIENT_ADDED_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, e.message ?: BaseResponse(
                        false,
                        Constants.Error.GENERAL
                    )
                )
            }
        }

        post("/update-client") {
            val clientRequest = call.receiveNullable<AddClientRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val client = ClientModel(
                    id = clientRequest.id ?: 0,
                    name = clientRequest.name,
                    lastname = clientRequest.lastname,
                    phone = clientRequest.phone
                )

                clientUseCase.updateClient(client = client)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.CLIENT_UPDATED_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, e.message ?: BaseResponse(
                        false,
                        Constants.Error.GENERAL
                    )
                )
            }
        }

        delete("/delete-client") {
            val clientRequest = call.request.queryParameters[Constants.Value.ID]?.toInt() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }
            try {
                clientUseCase.deleteClient(id = clientRequest)
                call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.CLIENT_DELETE_SUCESSSFULLY))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, e.message ?: BaseResponse(
                        false,
                        Constants.Error.GENERAL
                    )
                )
            }
        }

        get("/search-client") {
            val name = call.request.queryParameters["name"]
            val lastname = call.request.queryParameters["lastname"]

            if (name.isNullOrBlank() || lastname.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@get
            }

            try {
                val client = clientUseCase.getClientByNameAndLastname(name, lastname)
                if (client != null) {
                    call.respond(HttpStatusCode.OK, client)
                } else {
                    call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.CLIENT_DOESNT_EXIST))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }
    }
}