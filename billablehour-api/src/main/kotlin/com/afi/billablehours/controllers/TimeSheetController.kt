package com.afi.billablehours.controllers

//import org.springframework.hateoas.PagedResources
import com.afi.billablehours.config.Auditable
import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.TimeSheet
import com.afi.billablehours.models.requests.CreateTimeSheetEntryRequest
import com.afi.billablehours.services.TimeSheetService
import com.afi.billablehours.services.UserService
import com.afi.billablehours.utils.Constants
import com.afi.billablehours.utils.Constants.Companion.ERROR_INVALID_CLIENT
import com.afi.billablehours.utils.Constants.Companion.ERROR_INVALID_TIMESHEET
import com.afi.billablehours.utils.Constants.Companion.ERROR_TIMESHEET_CREATION
import com.afi.billablehours.utils.Constants.Companion.ERROR_TIMESHEET_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_TIMESHEET_CREATED
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_TIMESHEET_DETAIL
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_TIMESHEET_LIST
import com.afi.billablehours.utils.exceptions.ClientNotFoundException
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
class TimeSheetController(private val userService: UserService, private val timeSheetService: TimeSheetService) : Auditable() {

    // list
    /**
     * @api {get} /timesheets List all time sheet entries
     * @apiDescription List all time sheet entries for all lawyers
     * @apiGroup Grades
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "_embedded": {
     *      "timeSheets": [
     *          { 'employee': '', ... },
     *          { 'employee': '', ... },
     *          ...
     *      ]
     *  },
     *  "_links":{
     *      "first": {
     *          "href": "http://localhost:8080/api/timeSheets?page=0&size=2",
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
    @GetMapping(value = ["/timesheets"])
    fun list(pageable: Pageable, assembler: PagedResourcesAssembler<TimeSheet?>): ResponseEntity<*>? {
        val timeSheetPage: Page<TimeSheet?> = timeSheetService.listAll(pageable)
        val timeSheetPagedResources: PagedModel<EntityModel<TimeSheet?>> = assembler.toModel(timeSheetPage)
        return ResponseEntity<Any>(
                timeSheetPagedResources,
                HttpStatus.OK
        )
    }


    /**
     * @api {get} /list/timesheets List time sheets non-paginated
     * @apiDescription List time sheets non-paginated
     * @apiGroup TimeSheets
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "Some success message"
     *  "data": [
     *          { 'employee': '', ... },
     *          { 'employee': '', ... },
     *          ...
     *  ]
     * }
     */
    @GetMapping(value = ["/list/timesheets"])
    fun timeSheetsList(): ResponseEntity<*>? {

        val timeSheets: Iterable<TimeSheet?> = timeSheetService.listAll()
        return ResponseEntity<Any>(
                APIResponse(timeSheets, SUCCESS_TIMESHEET_LIST),
                HttpStatus.OK
        )

    }


    //create timesheet entry
    /**
     * @api {post} /timesheets Create timesheet entry
     * @apiDescription Create timesheet entry
     * @apiGroup Timesheets
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
     * @apiParam {String} companyId id of company
     * @apiParam {String} date date of project
     * @apiParam {String} startTime project start time
     * @apiParam {String} endTime project end time
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'startTime': 'Some time',
     *      ....
     *  },
     * "message": "Timesheet entry successfully created"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PostMapping(value = ["/timesheets"])
    fun create(@RequestBody @Valid request: CreateTimeSheetEntryRequest, bindingResult: BindingResult): ResponseEntity<*>? {
        if (bindingResult.hasErrors()) {
            return Validator(bindingResult).validateWithResponse()
        }

        return if (!userService.isLawyer) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {

            return try {
                val timeSheet: TimeSheet = timeSheetService.create(request)
                ResponseEntity<Any?>(
                        APIResponse(timeSheet, SUCCESS_TIMESHEET_CREATED),
                        HttpStatus.OK
                )
            } catch (ex: ClientNotFoundException) {
                ResponseEntity<Any?>(
                        APIResponse<String?>(ex.message, ERROR_TIMESHEET_CREATION),
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
            } catch (ex: Exception) {
                ResponseEntity<Any?>(
                        APIResponse<String?>(ex.message, ERROR_TIMESHEET_CREATION),
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
            }


        }
    }


    // update timesheet entry
    /**
     * @api {put} /timesheets/:timesheetId Update timesheet
     * @apiDescription Update timesheet
     * @apiGroup Grades
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
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
    @PutMapping(value = ["/timesheets/{timesheetId}"])
    fun update(@PathVariable timesheetId: Long, @RequestBody @Valid timesheetReq: CreateTimeSheetEntryRequest, bindingResult: BindingResult): ResponseEntity<*>? {

        return if (!userService.isLawyer) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val timeSheet: Optional<TimeSheet?> = timeSheetService.findById(timesheetId)
            if (!timeSheet.isPresent) {
                return ResponseEntity<Any>(
                        APIResponse<String>(ERROR_TIMESHEET_NOT_FOUND(timesheetId), ERROR_INVALID_CLIENT),
                        HttpStatus.NOT_FOUND
                )
            }
            if (bindingResult.hasErrors()) return Validator(bindingResult).validateWithResponse()
            return timeSheetService.update(timesheetReq, timeSheet.get())
        }
    }


    // find timesheet entry detail
    /**
     * @api {get} /timesheets/:timesheetId Find timesheet entry detail
     * @apiDescription Get details of a grade
     * @apiGroup Grades
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
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
    @GetMapping(value = ["/timesheets/{timesheetId}"])
    fun detail(@PathVariable timesheetId: Long): ResponseEntity<*>? {
        val timeSheet: Optional<TimeSheet?> = timeSheetService.findById(timesheetId)
        return if (timeSheet.isPresent) {
            ResponseEntity<Any?>(
                    APIResponse(timeSheet.get(), SUCCESS_TIMESHEET_DETAIL),
                    HttpStatus.OK)
        } else {
            ResponseEntity<Any?>(
                    APIResponse<String>(ERROR_TIMESHEET_NOT_FOUND(timesheetId), ERROR_INVALID_TIMESHEET),
                    HttpStatus.NOT_FOUND
            )
        }

    }
}
