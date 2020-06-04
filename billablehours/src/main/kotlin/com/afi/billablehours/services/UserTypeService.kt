package com.afi.billablehours.services

import com.afi.billablehours.models.User
import com.afi.billablehours.models.UserType
import com.afi.billablehours.repositories.UserRepository
import com.afi.billablehours.repositories.UserTypeRepository
import com.afi.billablehours.utils.Constants.Companion.ADMIN_USER_TYPE_NAME
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.*


@Service
class UserTypeService(private val userRepository: UserRepository, private val userTypeRepository: UserTypeRepository) {

    private val logger = LoggerFactory.getLogger(UserTypeService::class.java)

    fun findByName(name: String?): Optional<UserType> {
        return userTypeRepository.findByName(name)
    }

    fun save(userType: UserType): UserType {
        return userTypeRepository.save(userType)
    }

    fun listAll(): List<UserType> {
        val email: String? = (SecurityContextHolder.getContext().authentication.principal as User).email
        val optionalUser: Optional<User> = userRepository.findByEmail(email)
        var userTypes: List<UserType> = ArrayList()
        if (optionalUser.isPresent) {
            val user = optionalUser.get()
            if (ADMIN_USER_TYPE_NAME.equals(user.userType?.name)) {
                userTypes = userTypeRepository.findAll()
            }
            return userTypes
        }
        return userTypes
    }
}
