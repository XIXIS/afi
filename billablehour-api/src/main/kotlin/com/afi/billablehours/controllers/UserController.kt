package com.afi.billablehours.controllers

import com.afi.billablehours.config.Auditable
import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.User
import com.afi.billablehours.models.UserType
import com.afi.billablehours.models.requests.ChangePasswordRequest
import com.afi.billablehours.models.requests.CreateUserRequest
import com.afi.billablehours.services.UserService
import com.afi.billablehours.services.UserTypeService
import com.afi.billablehours.utils.Constants.Companion.ERROR_DISABLE_MY_ACCOUNT
import com.afi.billablehours.utils.Constants.Companion.ERROR_INCORRECT_OLD_PASSWORD
import com.afi.billablehours.utils.Constants.Companion.ERROR_NON_EXISTENT_USER
import com.afi.billablehours.utils.Constants.Companion.ERROR_PERMISSION_DENIED
import com.afi.billablehours.utils.Constants.Companion.ERROR_USER_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_VALIDATION
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_PASSWORD_UPDATED
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_USER_DETAIL
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_USER_TYPES_LIST
import com.afi.billablehours.validators.Validator
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull
import java.util.*

@RestController
@RequestMapping(value = ["api/v1"])
class UserController(private val userService: UserService, private val userTypeService: UserTypeService) : Auditable() {

    // list
    /**
     * @api {get} /users List users
     * @apiDescription List users - user type determines result list
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     * @apiVersion 0.0.1
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "_embedded": {
     *      "users": [
     *          { 'firstName': '', ... },
     *          { 'firstName': '', ... },
     *          ...
     *      ]
     *  },
     *  "_links":{
     *      "first": {
     *          "href": "http://localhost:8080/api/v1/users?page=0&size=2",
     *      },
     *      ...
     *  },
     *  "page": {
     *      "size": 2,
     *      "totalElements": 4,
     *      "totalPages": 2,
     *      "number": 0
     *  }
     * }
     */
    @GetMapping(value = ["/users"])
    fun list(pageable: Pageable, assembler: PagedResourcesAssembler<User?>): ResponseEntity<*> {

        return if (userService.isLawyer) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val userPage: Page<User?> = userService.listAll(pageable)
            val userPagedResources: PagedModel<EntityModel<User?>> = assembler.toModel(userPage)
            println(userPagedResources)
            ResponseEntity<Any>(
                    userPagedResources,
                    HttpStatus.OK
            )
        }
    }


    /**
     * @api {get} /list/users List users non-paginated
     * @apiDescription List users non-paginated - user type determines result list
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     * @apiVersion 0.0.1
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * HTTP/1.1 200 OK
     * {
     *  "message": "User listed"
     *  "users": [
     *      { 'firstName': '', ... },
     *      { 'firstName': '', ... },
     *      ...
     *  ]
     * }
     */
    @GetMapping(value = ["/list/users"])
    fun listUsers(): ResponseEntity<*> {
        return if (userService.isLawyer) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val users: List<User> = userService.list()
            ResponseEntity<Any>(
                    APIResponse(users, "User listed"),
                    HttpStatus.OK
            )
        }

    }

    // list user types
    /**
     * @api {get} /user-types List user types
     * @apiDescription List user types
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     * @apiVersion 0.0.1
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "Users Types Listed",
     *  "data": [{ 'name': '', ... },{ 'mame': '', ... }, ...]
     * }
     */
    @GetMapping(value = ["/user-types"])
    fun listTypes(): ResponseEntity<*> {
        val userTypes: List<UserType> = userTypeService.listAll()
        return if (!userService.isAdmin) {
            ResponseEntity(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else ResponseEntity<Any?>(
                APIResponse(userTypes, SUCCESS_USER_TYPES_LIST),
                HttpStatus.OK
        )
    }


    // create user
    /**
     * @api {post} /users Create users
     * @apiDescription Create users
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     * @apiVersion 0.0.1
     * @apiParam {String} firstName first name of user
     * @apiParam {String} lastName last name of user
     * @apiParam {String} email email of user
     * @apiParam {String} phone phone of user
     * @apiParam {Number} userTypeId user type id of user
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'firstName': 'Some',
     *      'lastName': 'Name',
     *      ....
     *  },
     *  "message": "User successfully created"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PostMapping(value = ["/users"])
    fun create(@Valid @RequestBody @NotNull request: CreateUserRequest, errors: Errors): ResponseEntity<*> {
        println(request.toString())
        if (errors.hasErrors()) return Validator(errors).validateWithResponse()
        return if (!userService.isAdmin) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else userService.register(request)
    }


    // update user
    /**
     * @api {put} /users/:userId Update user
     * @apiDescription Update user
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     * @apiVersion 0.0.1
     * @apiParam {String} firstName first name of user
     * @apiParam {String} lastName last name of user
     * @apiParam {String} email email of user
     * @apiParam {String} phone phone of user
     * @apiParam {Number} userTypeId user type id
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'firstName': 'Some',
     *      'lastName': 'Name',
     *      ...
     *  },
     *  "message": "User successfully updated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PutMapping(value = ["/users/{userId}"])
    fun update(@PathVariable userId: Long, @RequestBody editUser: @Valid CreateUserRequest): ResponseEntity<*> {

        return if (!userService.isAdmin) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val optionalUser: Optional<User?> = userService.findById(userId)
            if (!optionalUser.isPresent) {
                return ResponseEntity<Any?>(
                        APIResponse<String>(ERROR_NON_EXISTENT_USER, HttpStatus.NOT_FOUND.reasonPhrase),
                        HttpStatus.NOT_FOUND
                )
            }
            userService.update(editUser, optionalUser.get())
        }
    }


    /**
     * @api {put} /users/my/profile Update my profile
     * @apiDescription Update my profile
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     * @apiVersion 0.0.1
     * @apiParam {String} firstName first name of user
     * @apiParam {String} lastName last name of user
     * @apiParam {String} email email of user
     * @apiParam {String} phone phone of user
     * @apiParam {Number} userTypeId user type id
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'firstName': 'Some',
     *      'lastName': 'Name',
     *      ...
     *  },
     *  "message": "User successfully updated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PutMapping(value = ["/users/my/profile"])
    fun updateMyProfile(@RequestBody editUser: @Valid CreateUserRequest): ResponseEntity<*> {
        return userService.update(editUser, userService.authUser!!)
    }

    //Change password
    /**
     * @api {put} /users/change/password Change password
     * @apiDescription Change my password
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0;"
     * }
     * @apiVersion 0.0.1
     * @apiParam {String} password new user password
     * @apiParam {String} oldPassword old user password
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "User password successfully updated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PutMapping(value = ["/users/change/password"])
    fun updatePassword(@RequestBody @Valid changePasswordRequest: ChangePasswordRequest): ResponseEntity<*> {
        val user: User? = userService.authUser
        if (!userService.checkIfValidOldPassword(user, changePasswordRequest.oldPassword)) {
            return ResponseEntity<Any>(
                    APIResponse<String>(ERROR_INCORRECT_OLD_PASSWORD, ERROR_INCORRECT_OLD_PASSWORD),
                    HttpStatus.UNPROCESSABLE_ENTITY
            )
        }
        val nUser: User = userService.updatePassword(changePasswordRequest.password, user!!)
        return ResponseEntity<Any?>(
                APIResponse(nUser, SUCCESS_PASSWORD_UPDATED),
                HttpStatus.OK
        )
    }

    // find user detail
    /**
     * @api {get} /users/:userId Find user detail
     * @apiDescription Get details of a user
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     * @apiVersion 0.0.1
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "Users listed",
     *  "data": { 'firstName': '', ... }
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @GetMapping(value = ["/users/{userId}"])
    fun detail(@PathVariable userId: Long): ResponseEntity<*> {

        return if (!userService.isAdmin) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val user: Optional<User?> = userService.findById(userId)
            if (user.isPresent) {
                return ResponseEntity<Any?>(
                        APIResponse(user.get(), SUCCESS_USER_DETAIL),
                        HttpStatus.OK)
            }
            ResponseEntity<Any>(
                    APIResponse<String>(ERROR_NON_EXISTENT_USER, HttpStatus.NOT_FOUND.reasonPhrase),
                    HttpStatus.NOT_FOUND
            )
        }
    }

    // find detail of user making request
    /**
     * @api {get} /users/my/profile Find detail of authenticated user
     * @apiDescription Get details of authenticated user
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     *
     * @apiGroup Users
     * @apiVersion 0.0.1
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "User Profile",
     *  "data": { 'firstName': '', ... }
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @GetMapping(value = ["/users/my/profile"])
    fun myDetail(): ResponseEntity<*> {
        return try {
            val user: User? = userService.authUser
             ResponseEntity<Any?>(
                    APIResponse(user, SUCCESS_USER_DETAIL),
                    HttpStatus.OK)
        }catch (ex: Exception){
            ResponseEntity<Any?>(
                    APIResponse<String?>(ex.message, SUCCESS_USER_DETAIL),
                    HttpStatus.OK)
        }
    }


    // disable user
    /**
     * @api {put} /users/:userId/change-status Disable/Activate user
     * @apiDescription Disable/Activate user
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     * @apiVersion 0.0.1
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "User successfully disabled/activated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 403 Forbidden
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PutMapping(value = ["/users/{userId}/change-status"])
    fun disable(@PathVariable userId: Long): ResponseEntity<*> {
        val authId: Long = userService.authUser?.id!!
        if (authId == userId) {
            return ResponseEntity<Any>(
                    APIResponse<String>(ERROR_PERMISSION_DENIED, ERROR_DISABLE_MY_ACCOUNT),
                    HttpStatus.FORBIDDEN
            )
        }
        val optionalUser: Optional<User?> = userService.findById(userId)
        if (!optionalUser.isPresent) {
            return ResponseEntity<Any>(
                    APIResponse<String>(ERROR_USER_NOT_FOUND(userId), ERROR_NON_EXISTENT_USER),
                    HttpStatus.NOT_FOUND
            )
        }
        val user: User = optionalUser.get()
        val action: String = if (user.enabled) "disabled" else "enabled"
        user.enabled = !user.enabled
        userService.save(user)
        return ResponseEntity<Any>(
                APIResponse<String>("User successfully $action"),
                HttpStatus.OK
        )

    }

}
