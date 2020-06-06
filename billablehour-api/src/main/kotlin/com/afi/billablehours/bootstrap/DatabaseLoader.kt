package com.afi.billablehours.bootstrap

import com.afi.billablehours.models.User
import com.afi.billablehours.models.UserType
import com.afi.billablehours.repositories.UserRepository
import com.afi.billablehours.repositories.UserTypeRepository
import com.afi.billablehours.utils.Constants.Companion.ADMIN_USER_TYPE_NAME
import com.afi.billablehours.utils.Constants.Companion.FINANCE_USER_TYPE_NAME
import com.afi.billablehours.utils.Constants.Companion.LAWYER_USER_TYPE_NAME
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashSet

@Component
class DatabaseLoader(private val userTypeRepository: UserTypeRepository, private val userRepository: UserRepository,
                     private val passwordEncoder: BCryptPasswordEncoder): CommandLineRunner {


    override fun run(vararg args: String?) {

        if (!userTypeRepository.findById(1L).isPresent) {

            // add users and user types
            addUsersAndUserTypes()

        }
    }

    open fun addUsersAndUserTypes() {
        val secret = passwordEncoder.encode("password")
        val adminUserType = userTypeRepository.save(UserType(ADMIN_USER_TYPE_NAME))
        userTypeRepository.save(UserType(LAWYER_USER_TYPE_NAME))
        userTypeRepository.save(UserType(FINANCE_USER_TYPE_NAME))

        //SAVE USER TYPES

        //SAVE USERS
        val superUser = User(
                "Super",
                "Admin",
                "0207413037",
                "admin@billinghours.com",
                secret,  // password
                adminUserType //user type
        )
        userRepository.save(superUser)
    }
}