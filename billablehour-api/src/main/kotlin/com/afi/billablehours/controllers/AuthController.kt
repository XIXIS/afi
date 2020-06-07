package com.afi.billablehours.controllers

import com.afi.billablehours.config.JWTTokenUtil
import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.User
import com.afi.billablehours.models.requests.LoginRequest
import com.afi.billablehours.services.UserService
import com.afi.billablehours.utils.Constants.Companion.ERROR_DISABLED_USER
import com.afi.billablehours.utils.Constants.Companion.ERROR_NON_EXISTENT_USER
import com.afi.billablehours.utils.Constants.Companion.ERROR_SERVER
import com.afi.billablehours.utils.Constants.Companion.ERROR_WRONG_CREDENTIALS
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_LOGIN
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.validation.Valid

@RestController
@CrossOrigin
@RequestMapping(value=["api/v1"])
class AuthController(private val authenticationManager: AuthenticationManager, private val userService: UserService,
                        private val jWTTokenUtil: JWTTokenUtil) {


    /**
     * @api {post} /auth/login Login
     * @apiDescription Login
     * @apiGroup Auth
     * @apiVersion 0.1.0
     *
     * @apiParam {String} email email of user
     * @apiParam {String} password password of user
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'firstName': 'Some',
     *      'lastName': 'Name',
     *      ....
     *  },
     *  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0",
     *  "message": "Login successful"
     * }
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
    @PostMapping(value = ["/auth/login"])
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        return authenticate(loginRequest)
    }

    private fun authenticate(loginRequest: LoginRequest): ResponseEntity<Any> {
        return try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password))
            println("authentication successful----")
            val optionalUser: Optional<User> = userService.findByEmail(loginRequest.email)
            val token: String
            if (optionalUser.isPresent) {
                val user: User = optionalUser.get()
                token = jWTTokenUtil.generateToken(user)
                return ResponseEntity(APIResponse(user, token, SUCCESS_LOGIN), HttpStatus.OK)
            }
            throw UsernameNotFoundException("User does not exist")
        } catch (e: UsernameNotFoundException) {
            ResponseEntity<Any>(APIResponse<String?>(e.message, ERROR_NON_EXISTENT_USER), HttpStatus.NOT_FOUND)
        } catch (e: DisabledException) {
            ResponseEntity<Any>(APIResponse<String?>(e.message, ERROR_DISABLED_USER), HttpStatus.LOCKED)
        } catch (e: BadCredentialsException) {
            println(e.message)
            ResponseEntity(APIResponse<String?>(e.message, ERROR_WRONG_CREDENTIALS), HttpStatus.UNPROCESSABLE_ENTITY)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            ResponseEntity<Any>(APIResponse<String?>(e.message, ERROR_SERVER), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    /**
     * @api {post} /auth/password/reset Reset Password
     * @apiDescription Password Reset for Forgot Password
     * @apiGroup Auth
     * @apiVersion 0.1.0
     *
     * @apiParam {String} password new validated password
     * @apiParam {String} resetToken reset token sent to user
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "message": "Your password was successfully reset. You may now login to your account"
     * }
     *
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
//    @PostMapping("/auth/password/reset")
//    fun resetPassword(@RequestBody changePasswordRequest: @Valid ForgotSetPasswordRequest?): ResponseEntity<*> {
//        val optionalUser: Optional<User> = userService.findByResetToken(changePasswordRequest.getResetToken())
//        if (!optionalUser.isPresent()) {
//            return ResponseEntity<Any>(
//                    APIResponse(
//                            "Invalid password reset token",
//                            "Password reset token is invalid"),
//                    HttpStatus.UNPROCESSABLE_ENTITY
//            )
//        }
//        val user: User = optionalUser.get()
//        if (Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
//                        .isAfter(user.getActivationCodeExpiryDate())) {
//            return ResponseEntity<Any>(
//                    APIResponse(
//                            "Password reset token has expired",
//                            "Password reset token has expired"),
//                    HttpStatus.UNPROCESSABLE_ENTITY
//            )
//        }
//        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()))
//        userService.save(user)
//        return ResponseEntity<Any>(APIResponse("User password has been successfully reset"), HttpStatus.OK)
//    }
}

