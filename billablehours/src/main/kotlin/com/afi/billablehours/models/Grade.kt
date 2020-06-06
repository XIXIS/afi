package com.afi.billablehours.models

import com.afi.billablehours.config.Auditable
import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import lombok.ToString
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.Collections.singletonList
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Entity
@ToString
@Table(name = "grades")
class Grade() : Auditable() {

    constructor(name: String?, alias: String?, rate: Double?) : this() {
        this.name = name
        this.alias = alias
        this.rate = rate
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @NotEmpty(message = "Name is required.")
    @Column(nullable = false)
    var name: String? = null

    @NotEmpty(message = "Alias is required.")
    @Column(nullable = false, unique = true)
    var alias: String? = null

    @NotEmpty(message = "Phone is required.")
    @Column(nullable = false)
    var rate: Double? = null


}