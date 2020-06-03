package com.afi.billablehours.config

import com.afi.billablehours.models.User
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class AuditorAwareImpl : AuditorAware<Long> {
    override fun getCurrentAuditor(): Optional<Long> {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || authentication.principal == "anonymousUser") return Optional.empty()
        return if ((authentication.principal as UserDetails).username.contains("@")) Optional.ofNullable((authentication.principal as User).id) else Optional.empty()

    }
}