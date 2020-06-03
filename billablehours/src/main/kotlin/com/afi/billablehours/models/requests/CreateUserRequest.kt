package com.afi.billablehours.models.requests

import lombok.*
import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@RequiredArgsConstructor
@NoArgsConstructor
@ToString
abstract class CreateUserRequest : Serializable {
    val firstName: @NotEmpty(message = "First Name is required.") String? = null
    val lastName: @NotEmpty(message = "Last Name is required.") String? = null
    val phone: @NotEmpty(message = "Phone is required.") String? = null
    val email: @Email @NotEmpty(message = "Email is required.") String? = null
    abstract val userTypeId: Long
}