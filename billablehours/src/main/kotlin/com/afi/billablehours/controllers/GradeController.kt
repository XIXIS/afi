package com.afi.billablehours.controllers

//import org.springframework.hateoas.PagedResources
import com.afi.billablehours.config.Auditable
import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Grade
import com.afi.billablehours.models.requests.CreateGradeRequest
import com.afi.billablehours.services.GradeService
import com.afi.billablehours.services.UserService
import com.afi.billablehours.utils.Constants
import com.afi.billablehours.utils.Constants.Companion.ERROR_GRADE_CREATION
import com.afi.billablehours.utils.Constants.Companion.ERROR_GRADE_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_INVALID_COMPANY
import com.afi.billablehours.utils.Constants.Companion.ERROR_NON_EXISTENT_COMPANY
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_GRADES_LIST
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_GRADE_CREATED
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_GRADE_DETAIL
import com.afi.billablehours.validators.Validator
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["api/v1"])
class GradeController(private val userService: UserService, private val gradeService: GradeService) : Auditable() {

    // list
    /**
     * @api {get} /grades List grades
     * @apiDescription List all lawyers' grades
     * @apiGroup Grades
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "_embedded": {
     *      "grades": [
     *          { 'name': '', ... },
     *          { 'name': '', ... },
     *          ...
     *      ]
     *  },
     *  "_links":{
     *      "first": {
     *          "href": "http://localhost:8080/api/grades?page=0&size=2",
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
    @GetMapping(value = ["/grades"])
    fun list(pageable: Pageable, assembler: PagedResourcesAssembler<Grade?>): ResponseEntity<*>? {
        return if (!userService.isAdmin) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val gradePage: Page<Grade?> = gradeService.listAll(pageable)
            val gradePagedResources: PagedModel<EntityModel<Grade?>> = assembler.toModel(gradePage)
            return ResponseEntity<Any>(
                    gradePagedResources,
                    HttpStatus.OK
            )
        }
    }

    /**
     * @api {get} /list/grades List grades non-paginated
     * @apiDescription List grades non-paginated
     * @apiGroup Grades
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "Some success message"
     *  "data": [
     *          { 'name': '', ... },
     *          { 'name': '', ... },
     *          ...
     *  ]
     * }
     */
    @GetMapping(value = ["/list/grades"])
    fun gradesList(): ResponseEntity<*>? {

        return if (userService.isLawyer) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val grades: Iterable<Grade?> = gradeService.listAll()
            return ResponseEntity<Any>(
                    APIResponse(grades, SUCCESS_GRADES_LIST),
                    HttpStatus.OK
            )
        }
    }

    //create grade
    /**
     * @api {post} /grades Create grade
     * @apiDescription Create grade
     * @apiGroup Grades
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiParam {String} name name of company
     * @apiParam {String} rate email of company
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'name': 'Some',
     *      ....
     *  },
     * "message": "Grade successfully created"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PostMapping(value = ["/companies"])
    fun create(@RequestBody @Valid request: CreateGradeRequest, bindingResult: BindingResult): ResponseEntity<*>? {
        if (bindingResult.hasErrors()) { return Validator(bindingResult).validateWithResponse() }

        return if (userService.isLawyer) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val grade: Grade = gradeService.create(request)
            return try{
                ResponseEntity<Any?>(
                        APIResponse(gradeService.save(grade), SUCCESS_GRADE_CREATED),
                        HttpStatus.OK
                )
            }catch(ex: Exception){
                ResponseEntity<Any?>(
                        APIResponse(ex.message,ERROR_GRADE_CREATION),
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
            }


        }
    }


    // update grade
    /**
     * @api {put} /grades/:gradeId Update grade
     * @apiDescription Update grade
     * @apiGroup Grades
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiParam {String} name name of grade
     * @apiParam {String} rate billable rate
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'name': 'Some',
     *      ....
     *  },
     *  "message": "Grade successfully updated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PutMapping(value = ["/grades/{gradeId}"])
    fun update(@PathVariable gradeId: Long, @RequestBody @Valid editCompany: CreateGradeRequest, bindingResult: BindingResult): ResponseEntity<*>? {

        return if (userService.isLawyer) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val grade: Optional<Grade?> = gradeService.findById(gradeId)
            if (!grade.isPresent) {
                return ResponseEntity<Any>(
                        APIResponse<String>(ERROR_GRADE_NOT_FOUND(gradeId), ERROR_INVALID_COMPANY),
                        HttpStatus.NOT_FOUND
                )
            }
            if (bindingResult.hasErrors()) return Validator(bindingResult).validateWithResponse()
            return gradeService.update(editCompany, grade.get())
        }
    }


    // find company detail
    /**
     * @api {get} /grade/:gradeId Find grade detail
     * @apiDescription Get details of a grade
     * @apiGroup Grades
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "Grades listed",
     *  "data": { 'name': '', ... }
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @GetMapping(value = ["/grades/{gradeId}"])
    fun detail(@PathVariable gradeId: Long): ResponseEntity<*>? {
        val grade: Optional<Grade?> = gradeService.findById(gradeId)
        if (grade.isPresent) {
            return ResponseEntity<Any?>(
                    APIResponse(grade.get(), SUCCESS_GRADE_DETAIL),
                    HttpStatus.OK)
        }
        return ResponseEntity<Any?>(
                APIResponse<String>(ERROR_GRADE_NOT_FOUND(gradeId), ERROR_NON_EXISTENT_COMPANY),
                HttpStatus.NOT_FOUND
        )
    }


}
