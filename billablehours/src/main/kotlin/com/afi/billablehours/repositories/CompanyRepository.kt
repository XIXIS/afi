package com.afi.billablehours.repositories

import com.afi.billablehours.models.Company
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface CompanyRepository : PagingAndSortingRepository<Company?, Long?> {

    override fun findAll(): List<Company>
    override fun findAll(pageable: Pageable): Page<Company?>

    fun findAllByNameContainsOrEmailContainsOrPhoneContainsOrAddressContains(
            name: String?, email: String?, phone: String?, address: String?,
            pageable: Pageable?): Page<Company>


    @JvmDefault fun searchCompany(filter: String?, pageable: Pageable?): Page<Company> {
        return findAllByNameContainsOrEmailContainsOrPhoneContainsOrAddressContains(
                filter, filter, filter, filter, pageable
        )
    }

    fun findAllByNameContainsOrEmailContainsOrPhoneContainsOrAddressContains(
            name: String?, email: String?, phone: String?, address: String?): List<Company>


    @JvmDefault fun searchCompanyAsList(filter: String?): List<Company> {
        return findAllByNameContainsOrEmailContainsOrPhoneContainsOrAddressContains(
                filter, filter, filter, filter
        )
    }

}