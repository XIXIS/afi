package com.afi.billablehours.models

import com.fasterxml.jackson.annotation.JsonInclude

class APIResponse<U> {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private var error = ""

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private var message: String

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private var data: U? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private var token = ""

    constructor(error: String, message: String) {
        this.error = error
        this.message = message
    }

    constructor(message: String) {
        this.message = message
    }

    constructor(data: U, message: String) {
        this.message = message
        this.data = data
    }

    constructor(data: U, token: String, message: String) {
        this.message = message
        this.data = data
        this.token = token
    }
}