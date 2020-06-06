package com.afi.billablehours.services

import com.afi.billablehours.models.*
import com.afi.billablehours.models.requests.CreateCompanyRequest
import com.afi.billablehours.models.requests.CreateGradeRequest
import com.afi.billablehours.models.requests.CreateTimeSheetEntryRequest
import com.afi.billablehours.repositories.CompanyRepository
import com.afi.billablehours.repositories.TimeSheetRepository
import com.afi.billablehours.repositories.UserRepository
import com.afi.billablehours.utils.Constants
import com.afi.billablehours.utils.Constants.Companion.ERROR_COMPANY_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.LAWYER_USER_TYPE_NAME
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_COMPANY_CREATED
import com.afi.billablehours.utils.exceptions.CompanyNotFoundException
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
class TimeSheetService(private val userService: UserService, private val timeSheetRepository: TimeSheetRepository,
                       private val companyService: CompanyService) : Serializable {

    fun listAll(pageable: Pageable): Page<TimeSheet?> {
        val timeSheets: Page<TimeSheet?>
        val user: User? = userService.authUser
        timeSheets = when (user?.userType?.name) {
            LAWYER_USER_TYPE_NAME -> timeSheetRepository.findAllByUserId(user.id, pageable)
            else -> timeSheetRepository.findAll(pageable)
        }
        return timeSheets
    }

    fun listAll(): Iterable<TimeSheet?> {
        val timeSheets: Iterable<TimeSheet?>
        val user: User? = userService.authUser
        timeSheets = when (user?.userType?.name) {
            LAWYER_USER_TYPE_NAME -> timeSheetRepository.findAllByUserId(user.id)
            else -> timeSheetRepository.findAll()
        }
        return timeSheets
    }

    fun save(company: TimeSheet): TimeSheet {
        return timeSheetRepository.save(company)
    }


    fun create(request: CreateTimeSheetEntryRequest): TimeSheet {

        val company: Optional<Company?> = companyService.findById(request.companyId)
        if(!company.isPresent){
            throw CompanyNotFoundException(ERROR_COMPANY_NOT_FOUND(request.companyId))
        }
        val timeSheet = TimeSheet(userService.authUser, company.get(), request.date, request.startTime, request.endTime)
        return save(timeSheet)
    }

//
//    fun update(request: CreateTimeSheetEntry, timeSheetRepository: TimeSheetRepository): ResponseEntity<*>? {
//        company.name = request.name
//        company.email = request.email
//        company.phone = request.phone
//        company.address = request.address
//
//        return ResponseEntity<Any?>(
//                APIResponse(save(company), SUCCESS_COMPANY_CREATED),
//                HttpStatus.OK
//        )
//    }
//
//    fun save(company: Company): Company {
//        return companyRepository.save(company)
//    }
//


}
