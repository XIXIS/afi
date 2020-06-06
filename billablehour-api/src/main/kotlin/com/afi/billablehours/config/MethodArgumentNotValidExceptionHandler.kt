package com.afi.billablehours.config

import com.afi.billablehours.utils.Constants.Companion.ERROR_VALIDATION
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*


/**
 * Kudos http://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(ex: MethodArgumentNotValidException): Error {
        val result = ex.bindingResult
        val fieldErrors = result.fieldErrors
        return processFieldErrors(fieldErrors)
    }

    private fun processFieldErrors(fieldErrors: List<FieldError>): Error {
        val error = Error(HttpStatus.BAD_REQUEST.value(), ERROR_VALIDATION)
        for (fieldError in fieldErrors) {
            error.addFieldError(fieldError.field, fieldError.defaultMessage)
        }
        return error
    }

    companion object class Error(val status: Int, val message: String) {
        private val fieldErrors: MutableList<FieldError> = ArrayList()

        fun addFieldError(path: String, message: String?) {
            val error = FieldError(path, message!!, ERROR_VALIDATION)
            fieldErrors.add(error)
        }

        fun getFieldErrors(): List<FieldError> {
            return fieldErrors
        }

    }
}