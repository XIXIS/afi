package com.afi.billablehours.models.requests

import java.io.Serializable
import javax.validation.constraints.NotEmpty

data class LoginRequest(val email: @NotEmpty(message = "Email is required.") String?, val password: @NotEmpty(message = "Password is required.") String? = null) : Serializable