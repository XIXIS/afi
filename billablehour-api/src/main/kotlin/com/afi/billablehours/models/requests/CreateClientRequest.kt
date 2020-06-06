package com.afi.billablehours.models.requests

import org.springframework.validation.annotation.Validated
import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


@Validated
data class CreateClientRequest(@NotNull @NotEmpty(message = "Name is required.") val name: String,
                               @NotNull @NotEmpty(message = "Address is required.") val address:  String,
                               @NotNull @NotEmpty(message = "Phone is required.") val phone: String,
                               @NotNull @Email @NotEmpty(message = "Email is required.") val email: String) : Serializable

