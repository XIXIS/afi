package com.afi.billablehours.controllers

//import org.springframework.hateoas.PagedResources
import com.afi.billablehours.config.Auditable
import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Client
import com.afi.billablehours.models.requests.CreateClientRequest
import com.afi.billablehours.services.ClientService
import com.afi.billablehours.services.UserService
import com.afi.billablehours.utils.Constants.Companion.ERROR_CLIENT_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_INVALID_CLIENT
import com.afi.billablehours.utils.Constants.Companion.ERROR_NON_EXISTENT_CLIENT
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_CLIENTS_LIST
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_COMPANY_CREATED
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_CLIENT_DETAIL
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
class ClientController(private val clientService: ClientService) : Auditable() {

    // list
    /**
     * @api {get} /clients List clients
     * @apiDescription List clients
     * @apiGroup clients
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
     *      "clients": [
     *          { 'name': '', ... },
     *          { 'name': '', ... },
     *          ...
     *      ]
     *  },
     *  "_links":{
     *      "first": {
     *          "href": "http://localhost:8080/api/clients?page=0&size=2",
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
    @GetMapping(value = ["/clients"])
    fun list(pageable: Pageable, assembler: PagedResourcesAssembler<Client?>): ResponseEntity<*>? {
        val clientPage: Page<Client?> = clientService.listAll(pageable)
        val clientPagedResources: PagedModel<EntityModel<Client?>> = assembler.toModel(clientPage)
        return ResponseEntity<Any>(
                clientPagedResources,
                HttpStatus.OK
        )
    }

    /**
     * @api {get} /list/clients List clients non-paginated
     * @apiDescription List clients
     * @apiGroup clients
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
     *      { 'name': '', ... },
     *      { 'name': '', ... },
     *      ...
     *  ]
     * }
     */
    @GetMapping(value = ["/list/clients"])
    fun clientsList(): ResponseEntity<*>? {
        val clients: List<Client?> = clientService.listAllByList()
        return ResponseEntity<Any>(
                APIResponse(clients, SUCCESS_CLIENTS_LIST),
                HttpStatus.OK
        )
    }


    /**
     * @api {get} /search/clients?key= Search clients
     * @apiDescription Search clients
     * @apiGroup clients
     * @apiExample {curl} Example usage:
     * curl -i /clients/search?key=MTN
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
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
    @GetMapping(value = ["/search/clients"])
    fun listSearchedClients(@RequestParam(value = "key") key: String?): ResponseEntity<*>? {
        if (key == null || key == "") {
            return ResponseEntity<Any>(
                    APIResponse<String>("No search key specified", "Search key is required"),
                    HttpStatus.OK
            )
        }
        println("search key = $key")
        val clients: List<Client?> = clientService.listAllSearchedByList(key)
        return ResponseEntity<Any?>(
                APIResponse(clients, "Searched clients"),
                HttpStatus.OK
        )
    }


    //create client
    /**
     * @api {post} /clients Create client
     * @apiDescription Create client
     * @apiGroup clients
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
     * @apiParam {String} name name of client
     * @apiParam {String} email email of client
     * @apiParam {String} phone phone of client
     * @apiParam {String} address address of client
     *
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'name': 'Some',
     *      ....
     *  },
     * "    message": "Client successfully created"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PostMapping(value = ["/clients"])
    fun createCompany(@RequestBody @Valid request:  CreateClientRequest, bindingResult: BindingResult): ResponseEntity<*>? {
        if (bindingResult.hasErrors()) { return Validator(bindingResult).validateWithResponse() }
        val newClient: Client = clientService.create(request)
        return ResponseEntity<Any?>(
                APIResponse(clientService.save(newClient), SUCCESS_COMPANY_CREATED),
                HttpStatus.OK
        )
    }


    // update client
    /**
     * @api {put} /clients/:clientId Update client
     * @apiDescription Update client
     * @apiGroup clients
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.0.1
     * @apiParam {String} name name of client
     * @apiParam {String} email email of client
     * @apiParam {String} phone telephone number of client
     * @apiParam {String} address address of client
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "data": {
     *      'name': 'Some',
     *      ....
     *  },
     *  "message": "Client successfully updated"
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 422 Unprocessable Entity
     * {
     *  "error": "Some technical error message",
     *  "message": "Simple error message"
     * }
     */
    @PutMapping(value = ["/clients/{clientId}"])
    fun update(@PathVariable clientId: Long, @RequestBody @Valid editClient: CreateClientRequest, bindingResult: BindingResult): ResponseEntity<*>? {
        val optionalClient: Optional<Client?> = clientService.findById(clientId)
        if (!optionalClient.isPresent) {
            return ResponseEntity<Any>(
                    APIResponse<String>(ERROR_CLIENT_NOT_FOUND(clientId), ERROR_INVALID_CLIENT),
                    HttpStatus.NOT_FOUND
            )
        }
        if (bindingResult.hasErrors()) return Validator(bindingResult).validateWithResponse()
        return clientService.update(editClient, optionalClient.get())
    }


    // find client detail
    /**
     * @api {get} /clients/:clientId Find client detail
     * @apiDescription Get details of a client
     * @apiGroup clients
     * @apiHeader {String} Authorization Bearer Token
     * @apiHeaderExample {String} Header-Example:
     * {
     *  "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiRob255LmFkb2FzaUBjYWxsZW5zc29sdXRpb25zLmNvbSIs"
     * }
     * @apiVersion 0.1.0
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *  "message": "clients listed",
     *  "data": { 'name': '', ... }
     * }
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "Some technical error message",
     * "message": "Simple error message"
     * }
     */
    @GetMapping(value = ["/clients/{clientId}"])
    fun detail(@PathVariable clientId: Long): ResponseEntity<*>? {
        val client: Optional<Client?> = clientService.findById(clientId)
        if (client.isPresent) {
            return ResponseEntity<Any?>(
                    APIResponse(client.get(), SUCCESS_CLIENT_DETAIL),
                    HttpStatus.OK)
        }
        return ResponseEntity<Any?>(
                APIResponse<String>(ERROR_CLIENT_NOT_FOUND(clientId), ERROR_NON_EXISTENT_CLIENT),
                HttpStatus.NOT_FOUND
        )
    }


}
