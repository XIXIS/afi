package com.afi.billablehours.controllers

import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.User
import com.afi.billablehours.models.UserType
import com.afi.billablehours.services.UserService
import com.afi.billablehours.services.UserTypeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
//import org.springframework.hateoas.PagedResources
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
class UserController(private val userService: UserService, private val userTypeService: UserTypeService) {

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
     * @apiVersion 0.1.0
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
     *          "href": "http://localhost:8080/api/revenue-authorities?page=0&size=2",
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
//    @GetMapping(value = ["/users"])
//    fun list(@RequestParam(value = "filter", defaultValue = "") filter: String?,
//             pageable: Pageable?, assembler: PagedResourcesAssembler<User?>): ResponseEntity<*> {
//        val userPage: Page<User> = userService.listAll(pageable)
//        val userPagedResources: PagedResources = assembler.toResource(userPage)
//        return ResponseEntity<Any>(
//                userPagedResources,
//                HttpStatus.OK
//        )
//    }



    /**
     * @api {get} /users/list List users non-paginated
     * @apiDescription List users non-paginated- user type determines result list
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer djkaljdkfajdfaodpjoakf"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * HTTP/1.1 200 OK
     * {
     * "message": "User listed"
     * "users": [
     * { 'name': '', ... },
     * { 'name': '', ... },
     * ...
     * ]
     * }
     */
//    @GetMapping(value = ["/users/list"], produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun UsersList(): ResponseEntity<*> {
//        val users: List<User> = userService.listAllList()
//        return ResponseEntity<Any?>(
//                ListAPIResponse(users, "User listed"),
//                HttpStatus.OK
//        )
//    }

    // list user types
    /**
     * @api {get} /users List user types
     * @apiDescription List user types
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE1NjMyMTEwNTYsImV4cCI6MTU2MzIyOTA1Nn0"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "Users Types Listed",
     *  "data": [{ 'name': '', ... },{ 'mame': '', ... }, ...]
     * }
     */
    @GetMapping(value = ["/users/types"])
    fun listTypes(): ResponseEntity<*> {
        val userTypes: List<UserType> = userTypeService.listAll()
        return if (userTypes.isEmpty()) {
            ResponseEntity(
                    APIResponse<String>("Forbidden Action", "You are not permitted to access this resource"),
                    HttpStatus.FORBIDDEN
            )
        } else ResponseEntity<Any?>(
                APIResponse(userTypes, "User types listed"),
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
     * "Authorization": "Bearer djkaljdkfajdfaodpjoakf"
     * }
     * @apiVersion 0.1.0
     * @apiParam {String} firstName first name of user
     * @apiParam {String} lastName last name of user
     * @apiParam {String} email email of user
     * @apiParam {String} phone phone of user
     * @apiParam {Number} roleId role id of user
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "data": {
     * 'firstName': 'Some',
     * 'lastName': 'Name',
     * ....
     * },
     * "message": "User successfully created"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
//    @PostMapping(value = ["/users"])
//    fun create(@RequestBody request: @NotNull @Valid CreateUserRequest?,
//               bindingResult: @NotNull BindingResult?): ResponseEntity<*> {
//        System.out.println(request.toString())
//        if (bindingResult!!.hasErrors()) return Validator(bindingResult).validateWithResponse()
//        return if (!userService.hasPermission(userService.getAuthUser(), CREATE_USER)) {
//            userService.getPermissionDeniedResponse()
//        } else userService.register(request)
//    }


    // update user
    /**
     * @api {put} /users/:userId Update user
     * @apiDescription Update user
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer djkaljdkfajdfaodpjoakf"
     * }
     * @apiVersion 0.1.0
     * @apiParam {String} firstName first name of user
     * @apiParam {String} lastName last name of user
     * @apiParam {String} email email of user
     * @apiParam {String} phone phone of user
     * @apiParam {Number} roleId role id
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "data": {
     * 'firstName': 'Some',
     * 'lastName': 'Name',
     * .     ...
     * },
     * "message": "User successfully updated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
//    @PutMapping(value = ["/users/{userId}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
//    fun update(@PathVariable userId: Long, @RequestBody editUser: @Valid EditUserRequest?,
//               bindingResult: BindingResult): ResponseEntity<*> {
//        val optionalUser: Optional<User> = userService.findById(userId)
//        if (!optionalUser.isPresent()) {
//            activityLogService.logActivity("Attempted updating a USER", userService.getAuthUser(),
//                    "", "", ActivityLog.Status.FAILED, "Invalid USER (ID=$userId) Selected")
//            return ResponseEntity<Any?>(
//                    ObjectAPIResponse("User does not exist", "Invalid user selected"),
//                    HttpStatus.NOT_FOUND
//            )
//        }
//        if (bindingResult.hasErrors()) return Validator(bindingResult).validateWithResponse()
//        return if (!userService.hasPermission(userService.getAuthUser(), UPDATE_USER)) {
//            userService.getPermissionDeniedResponse()
//        } else userService.update(editUser, optionalUser.get())
//    }


    /**
     * @api {put} /users/my/profile Update my profile
     * @apiDescription Update my profile
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer djkaljdkfajdfaodpjoakf"
     * }
     * @apiVersion 0.1.0
     * @apiParam {String} firstName first name of user
     * @apiParam {String} lastName last name of user
     * @apiParam {String} email email of user
     * @apiParam {String} phone phone of user
     * @apiParam {Number} roleId role id
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "data": {
     * 'firstName': 'Some',
     * 'lastName': 'Name',
     * .     ...
     * },
     * "message": "User successfully updated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
//    @PutMapping(value = ["/users/my/profile"], consumes = [MediaType.APPLICATION_JSON_VALUE])
//    fun updateMyProfile(@RequestBody editUser: @Valid EditUserRequest?, bindingResult: BindingResult): ResponseEntity<*> {
//        return if (bindingResult.hasErrors()) Validator(bindingResult).validateWithResponse() else userService.update(editUser, userService.getAuthUser())
//    }

    //Change password
    /**
     * @api {put} /users/change/password Change password
     * @apiDescription Change my password
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer djkaljdkfajdfaodpjoakf;"
     * }
     * @apiVersion 0.1.0
     * @apiParam {String} password new user password
     * @apiParam {String} oldPassword old user password
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "message": "User password successfully updated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
//    @PutMapping(value = ["/users/change/password"], consumes = [MediaType.APPLICATION_JSON_VALUE])
//    fun updatePassword(@RequestBody changePasswordRequest: @Valid ChangePasswordRequest?): ResponseEntity<*> {
//        val user: User = userService.getAuthUser()
//        if (!userService.checkIfValidOldPassword(user, changePasswordRequest.getOldPassword())) {
//            activityLogService.logActivity("Attempted changing password", userService.getAuthUser(), "", "",
//                    ActivityLog.Status.FAILED, "Incorrect old password")
//            return ResponseEntity<Any>(
//                    APIResponse("Incorrect old password", "Validation Failed!"),
//                    HttpStatus.UNPROCESSABLE_ENTITY
//            )
//        }
//        val userSetting: UserSetting = user.getSetting()
//        if (userSetting != null) {
//            if (!userSetting.isHasChangedPassword()) {
//                userSetting.setHasChangedPassword(true)
//                settingService.saveUserSetting(userSetting)
//            }
//        }
//        user.setSetting(userSetting)
//        val nUser: User = userService.updatePassword(changePasswordRequest.getPassword(), user)
//        activityLogService.logActivity("Changed password", userService.getAuthUser(), "", "",
//                ActivityLog.Status.SUCCESS, "Password successfully updated!")
//        return ResponseEntity<Any?>(
//                ObjectAPIResponse(nUser, "Password successfully updated!"),
//                HttpStatus.OK
//        )
//    }

    // find user detail
    /**
     * @api {get} /users/:userId Find user detail
     * @apiDescription Get details of a user
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer djkaljdkfajdfaodpjoakf"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "message": "Users listed",
     * "data": { 'firstName': '', ... }
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
//    @GetMapping(value = ["/users/{userId}"])
//    fun detail(@PathVariable userId: Long): ResponseEntity<*> {
//        val user: Optional<User> = userService.findById(userId)
//        if (!userService.hasPermission(userService.getAuthUser(), VIEW_USER_PROFILE)) {
//            return userService.getPermissionDeniedResponse()
//        }
//        if (user.isPresent()) {
//            activityLogService.logActivity("Viewed profile of USER (NAME=" + user.get().getFullName().toString() + ")",
//                    userService.getAuthUser(), "", activityLogService.convertObjectToJson(LogDetail(user.get())),
//                    ActivityLog.Status.SUCCESS, "USER detail found")
//            return ResponseEntity<Any?>(
//                    ObjectAPIResponse(user.get(), "User detail found"),
//                    HttpStatus.OK)
//        }
//        activityLogService.logActivity("Attempted view profile of USER", userService.getAuthUser(),
//                "", "", ActivityLog.Status.FAILED, "Invalid USER (ID=$userId)  Selected")
//        return ResponseEntity<Any>(
//                APIResponse("User does not exist", "User detail could not be found"),
//                HttpStatus.NOT_FOUND
//        )
//    }

    // find detail of user making request
    /**
     * @api {get} /users/my/profile Find detail of authenticated user
     * @apiDescription Get details of authenticated user
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer djkaljdkfajdfaodpjoakf"
     * }
     *
     * @apiGroup Users
     * @apiVersion 0.1.0
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "message": "Users listed",
     * "data": { 'firstName': '', ... }
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
//    @GetMapping(value = ["/users/my/profile"])
//    fun myDetail(): ResponseEntity<*> {
//        val user: User = userService.getAuthUser()
//        activityLogService.logActivity("Viewed USER (NAME=" + user.getFullName().toString() + ") profile", user,
//                "", "", ActivityLog.Status.SUCCESS, "USER detail found")
//        return ResponseEntity<Any?>(
//                ObjectAPIResponse(user, "User detail found"),
//                HttpStatus.OK)
//    }

    // delete user
    /**
     * @api {delete} /users/:userId Delete user
     * @apiDescription Delete user
     * @apiGroup Users
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer djkaljdkfajdfaodpjoakf"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "message": "User successfully deleted"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 403 Forbidden
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
//    @DeleteMapping(value = ["/users/{userId}"])
//    fun delete(@PathVariable userId: Long): ResponseEntity<*> {
//        val authId: Long = userService.getAuthUser().getId()
//        if (authId == userId) {
//            return ResponseEntity<Any>(
//                    APIResponse("Action denied", "You cannot delete yourself"),
//                    HttpStatus.FORBIDDEN
//            )
//        }
//        val optionalUser: Optional<User> = userService.findById(userId)
//        if (!optionalUser.isPresent()) {
//            activityLogService.logActivity("Attempted deleting a USER", userService.getAuthUser(), "", "",
//                    ActivityLog.Status.FAILED, "Invalid USER (ID=$userId) Selected")
//            return ResponseEntity<Any>(
//                    APIResponse("User not found", "User does not exist"),
//                    HttpStatus.NOT_FOUND
//            )
//        }
//        return if (userService.hasPermission(userService.getAuthUser(), DELETE_USER)) {
//            userService.delete(userId)
//            activityLogService.logActivity("Deleted USER (NAME=" + optionalUser.get().getFullName().toString() + ")",
//                    userService.getAuthUser(), activityLogService.convertObjectToJson(LogDetail(optionalUser.get())), "",
//                    ActivityLog.Status.SUCCESS, "User successfully deleted")
//            ResponseEntity<Any>(
//                    APIResponse("User successfully deleted"),
//                    HttpStatus.OK
//            )
//        } else {
//            userService.getPermissionDeniedResponse()
//        }
//    }

}
