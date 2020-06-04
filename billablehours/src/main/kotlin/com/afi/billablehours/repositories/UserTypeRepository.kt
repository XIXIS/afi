package com.afi.billablehours.repositories

import com.afi.billablehours.models.UserType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserTypeRepository : JpaRepository<UserType, Long?> {
    fun findByName(name: String?): Optional<UserType>
    fun findByNameNot(name: String?): List<UserType>
}
