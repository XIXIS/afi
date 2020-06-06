package com.afi.billablehours.models.requests

import java.io.Serializable
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateInvoiceRequest(@NotNull val clientId: Long) : Serializable