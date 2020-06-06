package com.afi.billablehours.validators

import com.afi.billablehours.models.APIResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors


class Validator() {

    private lateinit var bindingResult: BindingResult
    private lateinit var errors: Errors

    constructor(bindingResult: BindingResult ): this(){
        this.bindingResult = bindingResult
    }
    constructor(errors : Errors): this(){
        this.errors = errors
    }

    fun validate(): String {
        val error = StringBuilder()
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.allErrors
            for (err in errors) {
                if (err === errors[errors.size - 1]) {
                    error.append(err.defaultMessage)
                    break
                }
                error.append(err.defaultMessage)
                error.append(" ,")
            }
        }
        return error.toString()
    }

    fun validate2(): String {
        val error = StringBuilder()
        if (errors.hasErrors()) {
            val errors = errors.allErrors
            for (err in errors) {
                if (err === errors[errors.size - 1]) {
                    error.append(err.defaultMessage)
                    break
                }
                error.append(err.defaultMessage)
                error.append(" ,")
            }
        }
        return error.toString()
    }

    fun validateWithResponse(): ResponseEntity<*> {
        val error = validate()
        return ResponseEntity<Any>(
                APIResponse<String>(error, "Validation Failed! $error"),
                HttpStatus.UNPROCESSABLE_ENTITY
        )
    }

    fun validateWithResponse2(): ResponseEntity<*> {
        val error = validate2()
        return ResponseEntity<Any>(
                APIResponse<String>(error, "Validation Failed! $error"),
                HttpStatus.UNPROCESSABLE_ENTITY
        )
    }

}