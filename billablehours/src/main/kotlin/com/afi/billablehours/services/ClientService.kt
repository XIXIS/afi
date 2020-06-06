package com.afi.billablehours.services

import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Client
import com.afi.billablehours.models.requests.CreateClientRequest
import com.afi.billablehours.repositories.ClientRepository
import com.afi.billablehours.repositories.UserRepository
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_COMPANY_CREATED
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.*
import java.util.*

@Service
class ClientService(private val clientRepository: ClientRepository) : Serializable {

    fun listAll(pageable: Pageable): Page<Client?> {
        return clientRepository.findAll(pageable)
    }

    fun listAllByList(): List<Client?> {
        return clientRepository.findAll()
    }

    fun listAllBySearch(pageable: Pageable?, filter: String?): Page<Client> {
        return clientRepository.searchClient(filter, pageable)
    }


    fun listAllSearchedByList(name: String?): List<Client?> {
        return clientRepository.searchClientAsList(name)
    }

    fun findById(id: Long): Optional<Client?> {
        return clientRepository.findById(id)
    }

    fun update(request: CreateClientRequest, client: Client): ResponseEntity<*>? {
        client.name = request.name
        client.email = request.email
        client.phone = request.phone
        client.address = request.address

        return ResponseEntity<Any?>(
                APIResponse(save(client), SUCCESS_COMPANY_CREATED),
                HttpStatus.OK
        )
    }

    fun save(client: Client): Client {
        return clientRepository.save(client)
    }

    fun create(request: CreateClientRequest): Client {
        val newClient = Client(request.name, request.email, request.phone, request.address)
        return clientRepository.save(newClient)
    }


}
