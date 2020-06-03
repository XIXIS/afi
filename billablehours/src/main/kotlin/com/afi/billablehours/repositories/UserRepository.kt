package com.afi.billablehours.repositories

import com.afi.billablehours.models.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface UserRepository : PagingAndSortingRepository<User?, Long?> {

    fun findByEmail(email: String): Optional<User>
    fun findByPhone(phone: String): Optional<User>
    fun findAllByUserType_Name(name: String, pageable: Pageable?): Page<User>
    fun findAllByUserType_NameAndFirstNameContainsOrUserType_NameAndLastNameContainsOrUserType_NameAndEmailContainsOrUserType_NameAndPhoneContains(
            userTypeName: String, firstName: String,
            userTypeName1: String, lastName: String,
            userTypeName2: String, email: String,
            userTypeName3: String, phone: String,
            pageable: Pageable): Page<User>

    @JvmDefault fun searchUsersForUserType(userTypeName: String, filter: String, pageable: Pageable): Page<User> {
        return findAllByUserType_NameAndFirstNameContainsOrUserType_NameAndLastNameContainsOrUserType_NameAndEmailContainsOrUserType_NameAndPhoneContains(
                userTypeName, filter,
                userTypeName, filter,
                userTypeName, filter,
                userTypeName, filter, pageable
        )
    }

    fun findAllByUserType_Name(name: String?): List<User?>?

}