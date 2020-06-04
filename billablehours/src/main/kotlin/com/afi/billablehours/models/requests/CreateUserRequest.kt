package com.afi.billablehours.models.requests

import org.springframework.validation.annotation.Validated
import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


@Validated
data class CreateUserRequest(@NotNull @NotEmpty(message = "First Name is required.") val firstName: String,
                             @NotNull @NotEmpty(message = "Last Name is required.") val lastName:  String,
                             @NotNull @NotEmpty(message = "Phone is required.") val phone: String,
                             @NotNull @Email @NotEmpty(message = "Email is required.") val email: String,
                             @NotNull val userTypeId: Long) : Serializable

