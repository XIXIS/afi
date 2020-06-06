package com.afi.billablehours.models.requests

import java.io.Serializable
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class LoginRequest(@NotNull @NotEmpty(message = "Email is required.") val email: String,
                        @NotNull @NotEmpty(message = "Password is required.") val password:  String) : Serializable