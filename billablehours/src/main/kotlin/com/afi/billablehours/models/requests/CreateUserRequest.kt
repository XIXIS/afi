package com.afi.billablehours.models.requests

import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty


data class CreateUserRequest(val firstName: @NotEmpty(message = "First Name is required.") String?,
                             val lastName: @NotEmpty(message = "Last Name is required.") String?,
                             val phone: @NotEmpty(message = "Phone is required.") String?,
                             val email: @Email @NotEmpty(message = "Email is required.") String?,
                             val userTypeId: Long) : Serializable

