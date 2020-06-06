package com.afi.billablehours.repositories

import com.afi.billablehours.models.Client
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface ClientRepository : PagingAndSortingRepository<Client?, Long?> {

    override fun findAll(): List<Client>
    override fun findAll(pageable: Pageable): Page<Client?>

    fun findAllByNameContainsOrEmailContainsOrPhoneContainsOrAddressContains(
            name: String?, email: String?, phone: String?, address: String?,
            pageable: Pageable?): Page<Client>


    @JvmDefault fun searchClient(filter: String?, pageable: Pageable?): Page<Client> {
        return findAllByNameContainsOrEmailContainsOrPhoneContainsOrAddressContains(
                filter, filter, filter, filter, pageable
        )
    }

    fun findAllByNameContainsOrEmailContainsOrPhoneContainsOrAddressContains(
            name: String?, email: String?, phone: String?, address: String?): List<Client>


    @JvmDefault fun searchClientAsList(filter: String?): List<Client> {
        return findAllByNameContainsOrEmailContainsOrPhoneContainsOrAddressContains(
                filter, filter, filter, filter
        )
    }

}