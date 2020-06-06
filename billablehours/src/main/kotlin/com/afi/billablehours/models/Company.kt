package com.afi.billablehours.models

import com.afi.billablehours.config.Auditable
import lombok.ToString
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Entity
@ToString
@Table(name = "companies")
class Company() : Auditable() {

    constructor(name: String?, email: String?, phone: String?, address: String?): this(){
        this.name = name
        this.email = email
        this.phone = phone
        this.address = address
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @NotEmpty(message = "Name is required.")
    @Column(nullable = false)
    var name: String? = null

    @NotEmpty(message = "Address is required.")
    @Column(nullable = false)
    var address: String? = null

    @NotEmpty(message = "Phone is required.")
    @Column(nullable = false, unique = true)
    var phone: String? = null

    @Email
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Email is required.")
    var email: String? = null

}