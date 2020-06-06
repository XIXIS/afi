package com.afi.billablehours.models.requests

import org.springframework.validation.annotation.Validated
import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


@Validated
data class CreateGradeRequest(@NotNull @NotEmpty(message = "Name is required.") val name: String,
                              @NotNull @NotEmpty(message = "Rate is required.") val rate:  Double) : Serializable

