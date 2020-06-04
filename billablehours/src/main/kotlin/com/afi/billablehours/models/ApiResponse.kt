package com.afi.billablehours.models

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class APIResponse<U>(var error: String?, var message: String?, var data: U?, var token: String?) {

    constructor(error: String, message: String) : this(error, message, null, "") {
        this.error = error
        this.message = message
    }

    constructor(data: U, message: String) : this("", message, data, "") {
        this.message = message
        this.data = data
    }

    constructor(data: U, token: String, message: String) : this("", message, data, token) {
        this.message = message
        this.data = data
        this.token = token
    }
}