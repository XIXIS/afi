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
@Table(name = "users")
class User() : Auditable(), UserDetails {

    constructor(firstName: String?, lastName: String?, phone: String?, email: String?, password: String?, userType: UserType): this(){
        this.firstName = firstName
        this.lastName = lastName
        this.phone = phone
        this.email = email
        this.password = password
        this.userType = userType
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @NotEmpty(message = "First Name is required.")
    @Column(nullable = false)
    var firstName: String? = null

    @NotEmpty(message = "Last Name is required.")
    @Column(nullable = false)
    var lastName: String? = null

    @NotEmpty(message = "Phone is required.")
    @Column(nullable = false, unique = true)
    var phone: String? = null

    @Email
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Email is required.")
    var email: String? = null

    @Column(length = 150)
    @NotEmpty(message = "Password is required.")
    @JsonIgnore
    private var password: String? = null

    @OneToOne
    var userType: UserType? = null

    @Column(nullable = false)
    var enabled = true

    @Column(nullable = false)
    var hasChangedPassword = false

    @JsonIgnore
    override fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return if (userType != null)  singletonList(SimpleGrantedAuthority(userType!!.name)) else ArrayList()
    }

    override fun getUsername(): String? {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return enabled
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }




}