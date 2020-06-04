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


        //Error Messages
        const val ERROR_NON_EXISTENT_USER = "User does not exist"
        const val ERROR_DISABLED_USER = "User is disabled"
        const val ERROR_WRONG_CREDENTIALS = "Wrong credentials provided"
        const val ERROR_SERVER = "Wrong credentials provided"
        const val ERROR_USER_CREATION = "An error occurred when creating new user"
        const val ERROR_PHONE_EMAIL_MAY_EXIST = "Phone or email may already exist"
        const val ERROR_PERMISSION_DENIED = "You are not permitted to access this resource"
        const val ERROR_INVALID_USER_TYPE = "Invalid User Type selected"
        fun  ERROR_USER_TYPE_NOT_FOUND(id: Long?): String {
          return "No User Type found for id: $id"
        }

    }



}