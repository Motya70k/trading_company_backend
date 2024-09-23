package com.example.routes

import com.example.authentification.hash
import com.example.data.model.EmployeeModel
import com.example.data.model.getRoleByString
import com.example.data.model.getStringByRole
import com.example.data.model.requests.LoginRequest
import com.example.data.model.requests.RegisterRequest
import com.example.data.model.requests.UpdateEmployeeRequest
import com.example.data.model.response.BaseResponse
import com.example.domain.usecase.EmployeeUseCase
import com.example.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.employeeRoute(employeeUseCase: EmployeeUseCase) {

    val hashPassword = { p: String -> hash(password = p) }

    post("/login") {
        val loginRequest = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            return@post
        }

        try {
            val employee = employeeUseCase.findEmployeeByLogin(loginRequest.login)

            if (employee == null) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.WRONG_LOGIN))
            } else {
                if (employee.password == hashPassword(loginRequest.password)) {
                    call.respond(HttpStatusCode.OK, BaseResponse(true, employeeUseCase.generateToken(employeeModel = employee)))
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest, BaseResponse(
                            false,
                            Constants.Error.INCORRECT_PASSWORD
                        )
                    )
                }
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.Conflict, BaseResponse(
                    false,
                    e.message ?: Constants.Error.GENERAL
                )
            )
        }
    }

    authenticate("jwt") {

        get("/get-all-employees") {

            try {
                val employee = call.principal<EmployeeModel>()
                if (employee != null && employee.role.getStringByRole() == Constants.Role.DIRECTOR) {
                    val employees = employeeUseCase.getAllEmployees()
                    call.respond(HttpStatusCode.OK, employees)
                } else {
                    call.respond(HttpStatusCode.Forbidden, BaseResponse(false, Constants.Error.YOU_ARE_NOT_DIRECTOR))
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, BaseResponse(
                        false,
                        e.message ?: Constants.Error.GENERAL
                    )
                )
            }
        }

        get("/get-employee-info") {
            try {
                val employee = call.principal<EmployeeModel>()

                if (employee != null) {
                    call.respond(HttpStatusCode.OK, employee)
                } else {
                    call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.EMPLOYEE_NOT_FOUND))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            }
        }

        post("/update-employee") {
            val employeeRequest = call.receiveNullable<UpdateEmployeeRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            val employee = call.principal<EmployeeModel>()
            if (employee != null && employee.role.getStringByRole() == Constants.Role.DIRECTOR) {
                try {
                    val employee = EmployeeModel(
                        id = employeeRequest.id ?: 0,
                        login = employeeRequest.login,
                        password = hashPassword(employeeRequest.lastname),
                        name = employeeRequest.name,
                        lastname = employeeRequest.lastname,
                        phoneNumber = employeeRequest.phoneNumber,
                        role = employeeRequest.role.getRoleByString()
                    )

                    val existingEmployee = employeeUseCase.getEmployeeById(employee.id)

                    if (existingEmployee == null) {
                        call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.EMPLOYEE_NOT_FOUND))
                        return@post
                    }

                    val existingEmployeePhone = employeeUseCase.getEmployeeByPhone(employee.phoneNumber)

                    if (existingEmployeePhone != null && existingEmployeePhone.id != employee.id) {
                        call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.EMPLOYEE_PHONE_ALREADY_EXIST))
                        return@post
                    }

                    val existingEmployeeLogin = employeeUseCase.findEmployeeByLogin(employee.login)

                    if (existingEmployeeLogin != null && existingEmployeeLogin.id != employee.id) {
                        call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.EMPLOYEE_LOGIN_ALREADY_EXIST))
                        return@post
                    }
                    employeeUseCase.updateEmployee(employee = employee)
                    call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.EMPLOYEE_UPDATED_SUCCESSFULLY))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict, e.message ?: BaseResponse(
                            false,
                            Constants.Error.GENERAL
                        )
                    )
                }
            } else {
                call.respond(HttpStatusCode.Forbidden, BaseResponse(false, Constants.Error.YOU_ARE_NOT_DIRECTOR))
            }
        }

        delete("/delete-employee") {
            val employeeRequest = call.request.queryParameters[Constants.Value.ID]?.toInt() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }

            val employee = call.principal<EmployeeModel>()
            if (employee != null && employee.role.getStringByRole() == Constants.Role.DIRECTOR) {
                try {
                    val existingEmployee = employeeUseCase.getEmployeeById(employeeRequest)

                    if (existingEmployee == null) {
                        call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.EMPLOYEE_NOT_FOUND))
                        return@delete
                    }
                    employeeUseCase.deleteEmployee(employeeId = employeeRequest)
                    call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.EMPLOYEE_DELETED_SUCCESSFULLY))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict, e.message ?: BaseResponse(
                            false,
                            Constants.Error.GENERAL
                        )
                    )
                }
            } else {
                call.respond(HttpStatusCode.Forbidden, BaseResponse(false, Constants.Error.YOU_ARE_NOT_DIRECTOR))
            }
        }

        get("/search-employee") {
            val name = call.request.queryParameters["name"]
            val lastname = call.request.queryParameters["lastname"]

            if (name.isNullOrBlank() || lastname.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@get
            }

            val employee = call.principal<EmployeeModel>()
            if (employee != null && employee.role.getStringByRole() == Constants.Role.DIRECTOR) {
                try {
                    val employees = employeeUseCase.fetchEmployeeByNameAndLastname(name, lastname)
                    if (employees.isNotEmpty()) {
                        call.respond(HttpStatusCode.OK, employees)
                    } else {
                        call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.EMPLOYEE_NOT_FOUND))
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
                }
            } else {
                call.respond(HttpStatusCode.Forbidden, BaseResponse(false, Constants.Error.YOU_ARE_NOT_DIRECTOR))
            }
        }

        post("/register") {
            val registerRequest = call.receiveNullable<RegisterRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
                return@post
            }

            val employee = call.principal<EmployeeModel>()
            if (employee != null && employee.role.getStringByRole() == Constants.Role.DIRECTOR) {
                try {
                    val employee = EmployeeModel(
                        id = 0,
                        login = registerRequest.login,
                        password = hashPassword(registerRequest.password),
                        name = registerRequest.name,
                        lastname = registerRequest.lastname,
                        phoneNumber = registerRequest.phoneNumber,
                        role = registerRequest.role.getRoleByString()
                    )

                    val existingEmployeePhone = employeeUseCase.getEmployeeByPhone(employee.phoneNumber)

                    if (existingEmployeePhone != null) {
                        call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.EMPLOYEE_PHONE_ALREADY_EXIST))
                        return@post
                    }

                    val existingEmployeeLogin = employeeUseCase.findEmployeeByLogin(employee.login)

                    if (existingEmployeeLogin != null) {
                        call.respond(HttpStatusCode.NotFound, BaseResponse(false, Constants.Error.EMPLOYEE_LOGIN_ALREADY_EXIST))
                        return@post
                    }

                    employeeUseCase.createEmployee(employee)
                    call.respond(HttpStatusCode.OK, BaseResponse(true, employeeUseCase.generateToken(employeeModel = employee)))

                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.Conflict, BaseResponse(
                            false,
                            e.message ?: Constants.Error.GENERAL
                        )
                    )
                }
            }

        }
    }
}