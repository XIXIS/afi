package com.afi.billablehours.services

import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Company
import com.afi.billablehours.models.User
import com.afi.billablehours.models.UserType
import com.afi.billablehours.models.requests.CreateCompanyRequest
import com.afi.billablehours.repositories.CompanyRepository
import com.afi.billablehours.repositories.UserRepository
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_COMPANY_CREATED
import org.springframework.core.io.ClassPathResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.util.*
import javax.transaction.Transactional
import kotlin.collections.ArrayList

@Service
class CompanyService(private val userRepository: UserRepository, private val companyRepository: CompanyRepository) : Serializable {

    fun listAll(pageable: Pageable): Page<Company?> {
        return companyRepository.findAll(pageable)
    }

    fun listAllByList(): List<Company?> {
        return companyRepository.findAll()
    }

    fun listAllBySearch(pageable: Pageable?, filter: String?): Page<Company> {
        return companyRepository.searchCompany(filter, pageable)
    }


    fun listAllSearchedByList(name: String?): List<Company?> {
        return companyRepository.searchCompanyAsList(name)
    }

    fun findById(id: Long): Optional<Company?> {
        return companyRepository.findById(id)
    }

    fun update(request: CreateCompanyRequest, company: Company): ResponseEntity<*>? {
        company.name = request.name
        company.email = request.email
        company.phone = request.phone
        company.address = request.address

        return ResponseEntity<Any?>(
                APIResponse(save(company), SUCCESS_COMPANY_CREATED),
                HttpStatus.OK
        )
    }

    fun save(company: Company): Company {
        return companyRepository.save(company)
    }

    fun create(request: CreateCompanyRequest): Company {
        val newCompany: Company = Company(request.name, request.email, request.phone, request.address)
        return companyRepository.save(newCompany)
    }


}
