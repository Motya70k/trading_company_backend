package com.example.utils

class Constants {

    object Role {
        const val DIRECTOR = "director"
        const val EMPLOYEE = "employee"
    }

    object Error {
        const val GENERAL = "Something went wrong"
        const val WRONG_LOGIN = "Wrong login"
        const val INCORRECT_PASSWORD = "Incorrect password"
        const val MISSING_FIELDS = "Missing some fields"
        const val EMPLOYEE_NOT_FOUND = "Employee not found"
        const val ITEMID_DOESNT_EXIST = "ItemId doesnt exist"
        const val CLIENT_DOESNT_EXIST = "Client doesnt exist"
        const val ITEM_NOT_FOUND = "Item not found"
        const val PURCHASE_NOT_FOUND = "Purchase not found"
        const val ORDER_NOT_FOUND = "Order not found"
        const val ITEM_EXISTS = "Item with this name already exist"
        const val EMPLOYEE_PHONE_ALREADY_EXIST = "Employee with this phone number already exist"
        const val EMPLOYEE_LOGIN_ALREADY_EXIST = "Employee with this login already exist"
        const val ITEM_AMOUNT_SMALLER_THEN_QUANTITY = "Item amount smaller"
        const val ITEM_AMOUNT_NULL_OR_MINUS = "Item amount null or minus"
        const val ITEM_QUANTITY_ZERO_OR_NEGATIVE = "Item amount zero or negative"
        const val ITEM_AMOUNT_GREATER_THAN_QUANTITY = "Item amount greater than qunatity"
        const val YOU_ARE_NOT_DIRECTOR = "Your rights dont't apply"
    }

    object Success {
        const val ITEM_ADDED_SUCCESSFULLY = "Item added successfully"
        const val ITEM_UPDATE_SUCCESSFULLY = "Item updated successfully"
        const val ITEM_DELETE_SUCCESSFULLY = "Item deleted successfully"

        const val PURCHASE_ADDED_SUCCESSFULLY = "Purchase added successfully"
        const val PURCHASE_UPDATE_SUCCESSFULLY = "Purchase updated successfully"
        const val PURCHASE_DELETE_SUCCESSFULLY = "Purchase deleted successfully"

        const val CLIENT_ADDED_SUCCESSFULLY = "Client added successfully"
        const val CLIENT_UPDATED_SUCCESSFULLY = "Client updated successfully"
        const val CLIENT_DELETE_SUCESSSFULLY = "Client deleted succesfully"

        const val ORDER_ADDED_SUCCESSFULLY = "Order added successfully"
        const val ORDER_UPDATED_SUCCESSFULLY = "Order updated successfully"
        const val ORDER_DELETED_SUCCESSFULLY = "Order deleted successfully"

        const val EMPLOYEE_UPDATED_SUCCESSFULLY = "Employee updated successfully"
        const val EMPLOYEE_DELETED_SUCCESSFULLY = "Employee deleted successfully"
    }

    object Value {
        const val ID = "id"
    }
}