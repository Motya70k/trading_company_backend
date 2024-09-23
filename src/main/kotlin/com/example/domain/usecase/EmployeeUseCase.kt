package com.example.domain.usecase

import com.auth0.jwt.JWTVerifier
import com.example.authentification.JwtService
import com.example.data.model.EmployeeModel
import com.example.domain.repository.EmployeeRepository

class EmployeeUseCase(
    private val repositoryImpl: EmployeeRepository,
    private val jwtService: JwtService
) {

    suspend fun createEmployee(employeeModel: EmployeeModel) = repositoryImpl.insertEmployee(employeeModel = employeeModel)

    suspend fun findEmployeeByLogin(login: String) = repositoryImpl.getEmployeeByLogin(login = login)

    fun generateToken(employeeModel: EmployeeModel): String = jwtService.generateToken(employee = employeeModel)

    fun getJwtVerifier(): JWTVerifier = jwtService.getVerifier()

    suspend fun getAllEmployees(): List<EmployeeModel> {
        return repositoryImpl.getAllEmployees()
    }

    suspend fun deleteEmployee(employeeId: Int) {
        repositoryImpl.deleteEmployee(employeeId)
    }

    suspend fun updateEmployee(employee: EmployeeModel) {
        repositoryImpl.updateEmployee(employee)
    }

    suspend fun fetchEmployeeByNameAndLastname(name: String, lastname: String): List<EmployeeModel> {
        return repositoryImpl.fetchEmployeeByNameAndLastname(name, lastname)
    }

    suspend fun getEmployeeById(employeeId: Int): EmployeeModel? {
        return repositoryImpl.getEmployeeById(employeeId)
    }

    suspend fun getEmployeeByPhone(phone: String): EmployeeModel? {
        return repositoryImpl.getEmployeeByPhone(phone)
    }

}