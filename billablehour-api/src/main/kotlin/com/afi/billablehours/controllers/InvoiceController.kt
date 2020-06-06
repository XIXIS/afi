package com.afi.billablehours.controllers

//import org.springframework.hateoas.PagedResources
import com.afi.billablehours.config.Auditable
import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Invoice
import com.afi.billablehours.models.requests.CreateInvoiceRequest
import com.afi.billablehours.services.InvoiceService
import com.afi.billablehours.services.UserService
import com.afi.billablehours.utils.Constants
import com.afi.billablehours.utils.Constants.Companion.ERROR_CLIENT_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_INVOICE_CREATION
import com.afi.billablehours.utils.Constants.Companion.ERROR_INVOICE_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_NON_EXISTENT_CLIENT
import com.afi.billablehours.utils.Constants.Companion.ERROR_NON_EXISTENT_INVOICE
import com.afi.billablehours.utils.Constants.Companion.ERROR_NO_TIMESHEETS
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_CLIENTS_LIST
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_INVOICE_CREATED
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_INVOICE_DETAIL
import com.afi.billablehours.utils.exceptions.ClientNotFoundException
import com.afi.billablehours.utils.exceptions.TimesheetInvoicedException
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
class InvoiceController(private val invoiceService: InvoiceService, private val userService: UserService) : Auditable() {

    // list
    /**
     * @api {get} /invoices List invoices
     * @apiDescription List invoices
     * @apiGroup Invoices
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "_embedded": {
     *      "invoices": [
     *          { 'client': {}, ... },
     *          { 'name': {}, ... },
     *          ...
     *      ]
     *  },
     *  "_links":{
     *      "first": {
     *          "href": "http://localhost:8080/api/invoices?page=0&size=2",
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
    @GetMapping(value = ["/invoices"])
    fun list(pageable: Pageable, assembler: PagedResourcesAssembler<Invoice?>): ResponseEntity<*>? {

        return if (!userService.isFinanceUser) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val clientPage: Page<Invoice?> = invoiceService.listAll(pageable)
            val clientPagedResources: PagedModel<EntityModel<Invoice?>> = assembler.toModel(clientPage)
            return ResponseEntity<Any>(
                    clientPagedResources,
                    HttpStatus.OK
            )
        }
    }


    /**
     * @api {get} /list/invoices List invoices non-paginated
     * @apiDescription List invoices
     * @apiGroup Invoices
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     * "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "message"
     *  "data": [
     *      { 'client': '', ... },
     *      { 'client': '', ... },
     *      ...
     *  ]
     * }
     */
    @GetMapping(value = ["/list/invoices"])
    fun invoicesList(): ResponseEntity<*>? {

        return if (!userService.isFinanceUser) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val clients: Iterable<Invoice?> = invoiceService.listAll()
            ResponseEntity<Any>(
                    APIResponse(clients, SUCCESS_CLIENTS_LIST),
                    HttpStatus.OK
            )
        }
    }


    //create invoice
    /**
     * @api {post} /clients Generate invoice
     * @apiDescription Generate invoice
     * @apiGroup Invoices
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
     * @apiParam {String} clientid id of client
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'client': {name: '',...},
     *      ....
     *  },
     * "    message": "Invoice successfully generated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PostMapping(value = ["/invoices"])
    fun createInvoice(@RequestBody @Valid request: CreateInvoiceRequest, bindingResult: BindingResult): ResponseEntity<*>? {
        if (bindingResult.hasErrors()) {
            return Validator(bindingResult).validateWithResponse()
        }

        return if (!userService.isFinanceUser) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            return try {
                val invoice: Invoice = invoiceService.create(request)
                ResponseEntity<Any?>(
                        APIResponse(invoice, SUCCESS_INVOICE_CREATED),
                        HttpStatus.OK
                )
            } catch (ex: ClientNotFoundException) {
                ResponseEntity<Any?>(
                        APIResponse<String?>(ERROR_CLIENT_NOT_FOUND(request.clientId), ERROR_NON_EXISTENT_CLIENT),
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
            } catch (ex: TimesheetInvoicedException) {
                ResponseEntity<Any?>(
                        APIResponse<String?>(ex.message, ERROR_NO_TIMESHEETS),
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
            } catch (ex: Exception) {
                ResponseEntity<Any?>(
                        APIResponse<String?>(ex.message, ERROR_INVOICE_CREATION),
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
            }
        }
    }


    // find invoice detail
    /**
     * @api {get} /invoices/:invoiceId Find invoice detail
     * @apiDescription Get details of an invoice
     * @apiGroup Invoices
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "invoices detail found",
     *  "data": { 'client': {name: '',...}, ... }
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
    @GetMapping(value = ["/invoices/{invoiceId}"])
    fun detail(@PathVariable invoiceId: Long): ResponseEntity<*>? {

        return if (!userService.isFinanceUser) {
            ResponseEntity<Any>(
                    APIResponse<String>(HttpStatus.FORBIDDEN.reasonPhrase, Constants.ERROR_PERMISSION_DENIED),
                    HttpStatus.FORBIDDEN
            )
        } else {
            val invoice: Optional<Invoice?> = invoiceService.findById(invoiceId)
            if (invoice.isPresent) {
                return ResponseEntity<Any?>(
                        APIResponse(invoice.get(), SUCCESS_INVOICE_DETAIL),
                        HttpStatus.OK)
            }
            return ResponseEntity<Any?>(
                    APIResponse<String>(ERROR_INVOICE_NOT_FOUND(invoiceId), ERROR_NON_EXISTENT_INVOICE),
                    HttpStatus.NOT_FOUND
            )
        }
    }


}
