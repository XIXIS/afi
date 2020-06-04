package com.afi.billablehours.utils

class Constants {

    companion object {
        const val ADMIN_USER_TYPE_NAME = "ADMIN"
        const val LAWYER_USER_TYPE_NAME = "LAWYER"
        const val FINANCE_USER_TYPE_NAME = "FINANCE_USER"

        //Success Messages
        const val SUCCESS_LOGIN = "Login Successful"
        const val SUCCESS_USER_TYPES_LIST = "List of User Types"
        const val SUCCESS_USER_CREATED = "User Successfully created"
        const val SUCCESS_USER_DETAIL = "User details found"
        const val SUCCESS_USER_DETAIL_UPDATED = "User details successfully updated"
        const val SUCCESS_PASSWORD_UPDATED = "Password successfully updated"


        //Error Messages
        const val ERROR_NON_EXISTENT_USER = "User does not exist"
        const val ERROR_DISABLED_USER = "User is disabled"
        const val ERROR_WRONG_CREDENTIALS = "Wrong credentials provided"
        const val ERROR_SERVER = "Wrong credentials provided"
        const val ERROR_USER_CREATION = "An error occurred when creating new user"
        const val ERROR_USER_UPDATE = "An error occurred when updating user details"
        const val ERROR_DUPLICATE_NON_EXISTENT = "Duplicate entries for unique fields or non existent parameters for required fields"
        const val ERROR_PERMISSION_DENIED = "You are not permitted to access this resource"
        const val ERROR_INVALID_USER_TYPE = "Invalid User Type selected"
        const val ERROR_VALIDATION = "Validation Error"
        const val ERROR_INCORRECT_OLD_PASSWORD = "Incorrect old password"
        const val ERROR_DISABLE_MY_ACCOUNT = "You cannot disable your own account"
        fun  ERROR_USER_TYPE_NOT_FOUND(id: Long?): String {
          return "No User Type found for id: $id"
        }

        fun  ERROR_USER_NOT_FOUND(id: Long?): String {
            return "No User found for id: $id"
        }

    }



}