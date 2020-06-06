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
        const val SUCCESS_CLIENT_CREATED = "Client Successfully created"
        const val SUCCESS_USER_DETAIL = "User details found"
        const val SUCCESS_USER_DETAIL_UPDATED = "User details successfully updated"
        const val SUCCESS_PASSWORD_UPDATED = "Password successfully updated"
        const val SUCCESS_CLIENT_DETAIL = "Client details found"
        const val SUCCESS_CLIENTS_LIST = "List of clients"
        const val SUCCESS_GRADES_LIST = "List of grades"
        const val SUCCESS_GRADE_CREATED = "Grade successfully created"
        const val SUCCESS_GRADE_DETAIL = "Grade details found"
        const val SUCCESS_TIMESHEET_LIST = "List of timesheets"
        const val SUCCESS_TIMESHEET_UPDATED = "Timesheet successfully updated"
        const val SUCCESS_TIMESHEET_CREATED = "Timesheet entry successfully created"
        const val SUCCESS_TIMESHEET_DETAIL = "Timesheet details found"

        //Error Messages
        const val ERROR_NON_EXISTENT_USER = "User does not exist"
        const val ERROR_NON_EXISTENT_CLIENT = "Client does not exist"
        const val ERROR_DISABLED_USER = "User is disabled"
        const val ERROR_WRONG_CREDENTIALS = "Wrong credentials provided"
        const val ERROR_SERVER = "Wrong credentials provided"
        const val ERROR_USER_CREATION = "An error occurred when creating new user"
        const val ERROR_LAWYER_CREATION = "An error occurred when creating new user of type lawyer"
        const val ERROR_GRADE_CREATION = "An error occurred when creating new grade"
        const val ERROR_GRADE_EXPECTED = "A grade is expected for a lawyer"
        const val ERROR_TIMESHEET_CREATION = "An error occurred when creating new timesheet entry"
        const val ERROR_USER_UPDATE = "An error occurred when updating user details"
        const val ERROR_TIMESHEET_UPDATE = "An error occurred when updating timesheet entry details"
        const val ERROR_DUPLICATE_NON_EXISTENT = "Duplicate entries for unique fields or non existent parameters for required fields"
        const val ERROR_PERMISSION_DENIED = "You are not permitted to access this resource or perform this action"
        const val ERROR_INVALID_USER_TYPE = "Invalid User Type selected"
        const val ERROR_INVALID_GRADE = "Invalid grade selected"
        const val ERROR_INVALID_CLIENT = "Invalid client selected"
        const val ERROR_INVALID_TIMESHEET = "Invalid timesheet entry selected"
        const val ERROR_VALIDATION = "Validation Error"
        const val ERROR_INCORRECT_OLD_PASSWORD = "Incorrect old password"
        const val ERROR_DISABLE_MY_ACCOUNT = "You cannot disable your own account"
        fun  ERROR_USER_TYPE_NOT_FOUND(id: Long?): String {
          return "No User Type found for id: $id"
        }

        fun  ERROR_USER_NOT_FOUND(id: Long?): String {
            return "No User found for id: $id"
        }
        fun  ERROR_CLIENT_NOT_FOUND(id: Long?): String {
            return "No Client found for id: $id"
        }
        fun  ERROR_GRADE_NOT_FOUND(id: Long?): String {
            return "No Grade found for id: $id"
        }
        fun  ERROR_TIMESHEET_NOT_FOUND(id: Long?): String {
            return "No Timesheet entry found for id: $id"
        }


    }



}