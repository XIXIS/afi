package com.afi.billablehours.models.requests

import java.io.Serializable
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ChangePasswordRequest(@NotNull @NotEmpty(message = "Password is required.") val password: String,
                                 @NotNull @NotEmpty(message = "Password is required.") val oldPassword:  String) : Serializable