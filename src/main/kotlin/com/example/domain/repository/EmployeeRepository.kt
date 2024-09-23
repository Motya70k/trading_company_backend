package com.example.domain.repository

import com.example.data.model.EmployeeModel
import com.example.data.model.ItemModel

interface EmployeeRepository {

    suspend fun getEmployeeByLogin(login: String): EmployeeModel?

    suspend fun insertEmployee(employeeModel: EmployeeModel)

    suspend fun getEmployeeById(employeeId: Int): EmployeeModel?

    suspend fun getAllEmployees(): List<EmployeeModel>

    suspend fun updateEmployee(employee: EmployeeModel)

    suspend fun deleteEmployee(employeeId: Int)

    suspend fun fetchEmployeeByNameAndLastname(name: String, lastname: String): List<EmployeeModel>

    suspend fun getEmployeeByPhone(phone: String): EmployeeModel?
}