package com.afi.billablehours.services

import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.User
import com.afi.billablehours.models.UserType
import com.afi.billablehours.models.requests.CreateUserRequest
import com.afi.billablehours.repositories.UserRepository
import com.afi.billablehours.repositories.UserTypeRepository
import com.afi.billablehours.utils.Constants.Companion.ADMIN_USER_TYPE_NAME
import com.afi.billablehours.utils.Constants.Companion.FINANCE_USER_TYPE_NAME
import com.afi.billablehours.utils.Constants.Companion.LAWYER_USER_TYPE_NAME
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.*
import javax.transaction.Transactional

@Service
class UserService(private val userRepository: UserRepository, private val userTypeRepository: UserTypeRepository) : UserDetailsService, Serializable {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user: Optional<User> = userRepository.findByEmail(email)
        if (!user.isPresent) {
            throw UsernameNotFoundException(email)
        }
        return user.get()
    }

    fun findByEmail(email: String?): Optional<User> {
        return userRepository.findByEmail(email)
    }

    fun findById(id: Long): Optional<User?> {
        return userRepository.findById(id)
    }

    fun register(newUser: CreateUserRequest): ResponseEntity<Any?> {
        val user: User = User()
        user.firstName = newUser.firstName
        user.lastName = newUser.lastName
        user.phone = newUser.phone
        user.email = newUser.email
        user.enabled = true

        val userType : Optional<UserType?> = userTypeRepository.findById(newUser.userTypeId)

        // encrypt and save password
        user.password = BCryptPasswordEncoder().encode("password")

        // save user
        try {
            val savedUser: User = save(user)
            println("User successfully created--------")
            return ResponseEntity<Any?>(
                    APIResponse(savedUser, "User registered successfully"),
                    HttpStatus.OK
            )
        } catch (e: Exception) {
            println(e.message)
            return ResponseEntity<Any?>(
                    APIResponse<String>("Phone or email may already exist", "User registration FAILED"),
                    HttpStatus.UNPROCESSABLE_ENTITY
            )
        }
    }

//    fun update(user: EditUserRequest, existingUser: User): ResponseEntity<*> {
//        val prevName: String = existingUser.getFullName()
//        val prev: String = activityLogService.convertObjectToJson(LogDetail(existingUser))
//        existingUser.setFirstName(user.getFirstName())
//        existingUser.setLastName(user.getLastName())
//        existingUser.setPhone(user.getPhone())
//        existingUser.setEmail(user.getEmail())
//
//        // save user
//        try {
//            val savedUser: User = save(existingUser)
//            return ResponseEntity<Any?>(
//                    APIResponse(savedUser, "User details successfully updated"),
//                    HttpStatus.OK
//            )
//        } catch (e: Exception) {
//            return ResponseEntity<Any?>(
//                    APIResponse<>("Non unique phone or email", "Phone or email may already exist"),
//                    HttpStatus.UNPROCESSABLE_ENTITY
//            )
//        }
//    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    val authUser: User?
        get() = (SecurityContextHolder.getContext().authentication.principal as User)
                .email?.let { userRepository.findByEmail(it).get() }

    fun listAll(pageable: Pageable?): Page<User> {
        val users: Page<User>
        val user: User? = authUser
        when (user?.userType?.name) {
            ADMIN_USER_TYPE_NAME -> users = userRepository.findAllByUserType_Name(ADMIN_USER_TYPE_NAME, pageable)
            LAWYER_USER_TYPE_NAME -> users = userRepository.findAllByUserType_Name(LAWYER_USER_TYPE_NAME, pageable)
            else -> users = userRepository.findAllByUserType_Name(FINANCE_USER_TYPE_NAME, pageable)
        }
        return users
    }

    @Transactional
    fun isEmailAlreadyInUse(username: String): Boolean {
        var userInDb: Boolean = true
        if (getActiveUser(username) == null) userInDb = false
        return userInDb
    }

    @Transactional
    fun isPhoneAlreadyInUse(phone: String): Boolean {
        var userInDb: Boolean = true
        if (getActiveUserByPhone(phone) == null) userInDb = false
        return userInDb
    }

    private fun getActiveUser(email: String): User? {
        val user: User
        val optionalUser: Optional<User> = userRepository.findByEmail(email)
        user = optionalUser.orElse(null)
        return user
    }

    private fun getActiveUserByPhone(phone: String): User? {
        val user: User?
        val optionalUser: Optional<User> = userRepository.findByPhone(phone)
        user = optionalUser.orElse(null)
        return user
    }

    fun delete(userId: Long) {
        userRepository.deleteById(userId)
    }

    fun updatePassword(newPassword: String?, user: User): User {
        user.password = BCryptPasswordEncoder().encode(newPassword)
        return save(user)
    }


    val isAdmin: Boolean
        get() {
            return authUser?.userType?.name.equals(ADMIN_USER_TYPE_NAME)
        }

    val isLawyer: Boolean
        get() {
            return authUser?.userType?.name.equals(LAWYER_USER_TYPE_NAME)
        }

    val isFinanceUser: Boolean
        get() {
            return authUser?.userType?.name.equals(FINANCE_USER_TYPE_NAME)
        }


    fun checkIfValidOldPassword(user: User, oldPassword: String?): Boolean {
        return BCryptPasswordEncoder().matches(oldPassword, user.password)
    }
}
