package com.afi.billablehours.services

import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Grade
import com.afi.billablehours.models.User
import com.afi.billablehours.models.UserType
import com.afi.billablehours.models.requests.CreateUserRequest
import com.afi.billablehours.repositories.UserRepository
import com.afi.billablehours.repositories.UserTypeRepository
import com.afi.billablehours.utils.Constants.Companion.ADMIN_USER_TYPE_NAME
import com.afi.billablehours.utils.Constants.Companion.ERROR_INVALID_USER_TYPE
import com.afi.billablehours.utils.Constants.Companion.ERROR_DUPLICATE_NON_EXISTENT
import com.afi.billablehours.utils.Constants.Companion.ERROR_GRADE_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_INVALID_GRADE
import com.afi.billablehours.utils.Constants.Companion.ERROR_USER_CREATION
import com.afi.billablehours.utils.Constants.Companion.ERROR_USER_TYPE_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_USER_UPDATE
import com.afi.billablehours.utils.Constants.Companion.FINANCE_USER_TYPE_NAME
import com.afi.billablehours.utils.Constants.Companion.LAWYER_USER_TYPE_NAME
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_USER_CREATED
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_USER_DETAIL_UPDATED
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
import kotlin.collections.ArrayList

@Service
class UserService(private val userRepository: UserRepository, private val userTypeRepository: UserTypeRepository,
                  private val gradeService: GradeService) : UserDetailsService, Serializable {

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
        val userType : Optional<UserType?> = userTypeRepository.findById(newUser.userTypeId)
        if(!userType.isPresent)
            return ResponseEntity<Any?>(
                    APIResponse<String>(ERROR_USER_TYPE_NOT_FOUND(newUser.userTypeId), ERROR_INVALID_USER_TYPE),
                    HttpStatus.UNPROCESSABLE_ENTITY
            )
        user.userType = userType.get()

        // encrypt and save password
        user.password = BCryptPasswordEncoder().encode("password")

        if(newUser.gradeId!=null){
            val grade: Optional<Grade?> = gradeService.findById(newUser.gradeId!!)
            if (!grade.isPresent)
                return ResponseEntity<Any?>(
                        APIResponse<String>(ERROR_GRADE_NOT_FOUND(newUser.gradeId), ERROR_INVALID_GRADE),
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
            user.grade = grade.get()
        }

        // save user
        return try {
            val savedUser: User = save(user)
            println("User successfully created--------")
            ResponseEntity<Any?>(
                    APIResponse(savedUser, SUCCESS_USER_CREATED),
                    HttpStatus.OK
            )
        } catch (e: Exception) {
            println(e.message)
            ResponseEntity<Any?>(
                    APIResponse<String>(e.message!!, ERROR_USER_CREATION),
                    HttpStatus.UNPROCESSABLE_ENTITY
            )
        }
    }

    fun update(user: CreateUserRequest, existingUser: User): ResponseEntity<*> {

        existingUser.firstName = user.firstName
        existingUser.lastName = user.lastName
        existingUser.phone = user.phone
        existingUser.email = user.email

        if(existingUser.userType?.id != user.userTypeId) {
            val userType: Optional<UserType?> = userTypeRepository.findById(user.userTypeId)
            if (!userType.isPresent)
                return ResponseEntity<Any?>(
                        APIResponse<String>(ERROR_USER_TYPE_NOT_FOUND(user.userTypeId), ERROR_INVALID_USER_TYPE),
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
            existingUser.userType = userType.get()
        }

        if(existingUser.grade?.id != user.gradeId) {
            val grade: Optional<Grade?> = gradeService.findById(user.gradeId!!)
            if (!grade.isPresent)
                return ResponseEntity<Any?>(
                        APIResponse<String>(ERROR_GRADE_NOT_FOUND(user.gradeId), ERROR_INVALID_GRADE),
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
            existingUser.grade = grade.get()
        }

        // save user
        return try {
            val savedUser: User = save(existingUser)
            ResponseEntity<Any?>(
                    APIResponse(savedUser, SUCCESS_USER_DETAIL_UPDATED),
                    HttpStatus.OK
            )
        } catch (e: Exception) {
            ResponseEntity<Any?>(
                    APIResponse<String>(e.message!!, ERROR_USER_UPDATE),
                    HttpStatus.UNPROCESSABLE_ENTITY
            )
        }
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    val authUser: User?
        get() = (SecurityContextHolder.getContext().authentication.principal as User)
                .email?.let { userRepository.findByEmail(it).get() }

    fun listAll(pageable: Pageable): Page<User?> {
        val users: Page<User?>
        val user: User? = authUser
        users = when (user?.userType?.name) {
            ADMIN_USER_TYPE_NAME -> userRepository.findAll(pageable)
            FINANCE_USER_TYPE_NAME -> userRepository.findAllByUserType_Name(LAWYER_USER_TYPE_NAME, pageable)
            else -> userRepository.findAllByUserType_Name(FINANCE_USER_TYPE_NAME, pageable)
        }
        return users
    }

    fun list(): List<User> {
        val users: List<User>
        val user: User? = authUser
        users = when (user?.userType?.name) {
            ADMIN_USER_TYPE_NAME -> userRepository.findAll()
            FINANCE_USER_TYPE_NAME -> userRepository.findAllByUserTypeName(LAWYER_USER_TYPE_NAME)
            else -> ArrayList()
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
        user.hasChangedPassword = true
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


    fun checkIfValidOldPassword(user: User?, oldPassword: String): Boolean {
        return BCryptPasswordEncoder().matches(oldPassword, user?.password)
    }
}
