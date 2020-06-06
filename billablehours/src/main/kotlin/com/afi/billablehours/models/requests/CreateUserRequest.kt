package com.afi.billablehours.models.requests

import org.springframework.validation.annotation.Validated
import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


@Validated
data class CreateUserRequest(@NotNull @NotEmpty(message = "First Name is required.") var firstName: String,
                             @NotNull @NotEmpty(message = "Last Name is required.") var lastName:  String,
                             @NotNull @NotEmpty(message = "Phone is required.") var phone: String,
                             @NotNull @Email @NotEmpty(message = "Email is required.") var email: String,
                             @NotNull var userTypeId: Long) : Serializable{

    final var gradeId: Long? =null
    constructor(firstName: String,lastName:  String, phone: String, email: String, gradeId: Long, userTypeId: Long) :
            this(firstName, lastName, phone, email, userTypeId){
        this.firstName = firstName
        this.lastName = lastName
        this.phone = phone
        this.email = email
        this.userTypeId = userTypeId
        this.gradeId = gradeId
    }
}

