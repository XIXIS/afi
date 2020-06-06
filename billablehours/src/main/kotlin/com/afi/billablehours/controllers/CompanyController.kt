package com.afi.billablehours.controllers

//import org.springframework.hateoas.PagedResources
import com.afi.billablehours.config.Auditable
import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Company
import com.afi.billablehours.models.requests.CreateCompanyRequest
import com.afi.billablehours.services.CompanyService
import com.afi.billablehours.services.UserService
import com.afi.billablehours.utils.Constants.Companion.ERROR_COMPANY_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_INVALID_COMPANY
import com.afi.billablehours.utils.Constants.Companion.ERROR_NON_EXISTENT_COMPANY
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_COMPANIES_LIST
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_COMPANY_CREATED
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_COMPANY_DETAIL
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
class CompanyController(private val userService: UserService, private val companyService: CompanyService) : Auditable() {

    // list
    /**
     * @api {get} /companies List companies
     * @apiDescription List companies
     * @apiGroup Companies
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
     *      "companies": [
     *          { 'name': '', ... },
     *          { 'name': '', ... },
     *          ...
     *      ]
     *  },
     *  "_links":{
     *      "first": {
     *          "href": "http://localhost:8080/api/companies?page=0&size=2",
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
    @GetMapping(value = ["/companies"])
    fun list(pageable: Pageable, assembler: PagedResourcesAssembler<Company?>): ResponseEntity<*>? {
        val companyPage: Page<Company?> = companyService.listAll(pageable)
        val companyPagedResources: PagedModel<EntityModel<Company?>> = assembler.toModel(companyPage)
        return ResponseEntity<Any>(
                companyPagedResources,
                HttpStatus.OK
        )
    }

    /**
     * @api {get} /list/companies List companies non-paginated
     * @apiDescription List companies
     * @apiGroup Companies
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "message"
     *  "data": [
     *      { 'name': '', ... },
     *      { 'name': '', ... },
     *      ...
     *  ]
     * }
     */
    @GetMapping(value = ["/list/companies"])
    fun companiesList(): ResponseEntity<*>? {
        val companies: List<Company?> = companyService.listAllByList()
        return ResponseEntity<Any>(
                APIResponse(companies, SUCCESS_COMPANIES_LIST),
                HttpStatus.OK
        )
    }


    /**
     * @api {get} /search/companies?key= Search companies
     * @apiDescription Search companies
     * @apiGroup Companies
     * @apiExample {curl} Example usage:
     * curl -i /companies/search?key=MTN
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "message"
     *  "data": [
     *      { 'name': '', ... },
     *      { 'name': '', ... },
     *      ...
     *  ]
     * }
     */
    @GetMapping(value = ["/search/companies"])
    fun listSearchedCompanies(@RequestParam(value = "key") key: String?): ResponseEntity<*>? {
        if (key == null || key == "") {
            return ResponseEntity<Any>(
                    APIResponse<String>("No search key specified", "Search key is required"),
                    HttpStatus.OK
            )
        }
        println("search key = $key")
        val companies: List<Company?> = companyService.listAllSearchedByList(key)
        return ResponseEntity<Any?>(
                APIResponse(companies, "Searched companies"),
                HttpStatus.OK
        )
    }


    //create company
    /**
     * @api {post} /companies Create company
     * @apiDescription Create company
     * @apiGroup Companies
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiParam {String} name name of company
     * @apiParam {String} email email of company
     * @apiParam {String} phone phone of company
     * @apiParam {String} address address of company
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'name': 'Some',
     *      ....
     *  },
     * "    message": "Company successfully created"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PostMapping(value = ["/companies"])
    fun createCompany(@RequestBody @Valid request:  CreateCompanyRequest, bindingResult: BindingResult): ResponseEntity<*>? {
        if (bindingResult.hasErrors()) { return Validator(bindingResult).validateWithResponse() }
        val newCompany: Company = companyService.create(request)
        return ResponseEntity<Any?>(
                APIResponse(companyService.save(newCompany), SUCCESS_COMPANY_CREATED),
                HttpStatus.OK
        )
    }


    // update company
    /**
     * @api {put} /companies/:companyId Update company
     * @apiDescription Update company
     * @apiGroup Companies
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiParam {String} name name of company
     * @apiParam {String} email email of company
     * @apiParam {String} phone telephone number of company
     * @apiParam {String} address address of company
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'name': 'Some',
     *      ....
     *  },
     *  "message": "Company successfully updated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PutMapping(value = ["/companies/{companyId}"])
    fun update(@PathVariable companyId: Long, @RequestBody @Valid editCompany: CreateCompanyRequest, bindingResult: BindingResult): ResponseEntity<*>? {
        val optionalCompany: Optional<Company?> = companyService.findById(companyId)
        if (!optionalCompany.isPresent) {
            return ResponseEntity<Any>(
                    APIResponse<String>(ERROR_COMPANY_NOT_FOUND(companyId), ERROR_INVALID_COMPANY),
                    HttpStatus.NOT_FOUND
            )
        }
        if (bindingResult.hasErrors()) return Validator(bindingResult).validateWithResponse()
        return companyService.update(editCompany, optionalCompany.get())
    }


    // find company detail
    /**
     * @api {get} /companies/:companyId Find company detail
     * @apiDescription Get details of a company
     * @apiGroup Companies
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "Companies listed",
     *  "data": { 'name': '', ... }
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
    @GetMapping(value = ["/companies/{companyId}"])
    fun detail(@PathVariable companyId: Long): ResponseEntity<*>? {
        val company: Optional<Company?> = companyService.findById(companyId)
        if (company.isPresent) {
            return ResponseEntity<Any?>(
                    APIResponse(company.get(), SUCCESS_COMPANY_DETAIL),
                    HttpStatus.OK)
        }
        return ResponseEntity<Any?>(
                APIResponse<String>(ERROR_COMPANY_NOT_FOUND(companyId), ERROR_NON_EXISTENT_COMPANY),
                HttpStatus.NOT_FOUND
        )
    }


}
