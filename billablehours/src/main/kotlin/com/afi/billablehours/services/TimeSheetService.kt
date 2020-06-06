package com.afi.billablehours.services

import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Company
import com.afi.billablehours.models.TimeSheet
import com.afi.billablehours.models.User
import com.afi.billablehours.models.requests.CreateTimeSheetEntryRequest
import com.afi.billablehours.repositories.TimeSheetRepository
import com.afi.billablehours.utils.Constants.Companion.ERROR_COMPANY_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_TIMESHEET_UPDATE
import com.afi.billablehours.utils.Constants.Companion.LAWYER_USER_TYPE_NAME
import com.afi.billablehours.utils.exceptions.CompanyNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.*

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
        if (!company.isPresent) {
            throw CompanyNotFoundException(ERROR_COMPANY_NOT_FOUND(request.companyId))
        }
        val user: User? = userService.authUser
        val timeSheet = TimeSheet(user, user?.grade?.rate, company.get(), request.date, request.startTime, request.endTime)
        return save(timeSheet)
    }

    fun findById(id: Long): Optional<TimeSheet?> {
        return timeSheetRepository.findById(id)
    }


    fun update(timeSheetReq: CreateTimeSheetEntryRequest, timeSheet: TimeSheet): ResponseEntity<*>? {
        if (timeSheet.company?.id != timeSheetReq.companyId) {
            val company: Optional<Company?> = companyService.findById(timeSheetReq.companyId)
            if (!company.isPresent) {
                throw CompanyNotFoundException(ERROR_COMPANY_NOT_FOUND(timeSheetReq.companyId))
            }
            timeSheet.company = company.get()
        }
        timeSheet.date = timeSheetReq.date
        timeSheet.startTime = timeSheetReq.startTime
        timeSheet.endTime = timeSheetReq.endTime

        return try {
            ResponseEntity<Any?>(
                    APIResponse(save(timeSheet), "Timesheet successfully updated"),
                    HttpStatus.OK
            )
        } catch (ex: Exception) {
            ResponseEntity<Any?>(
                    APIResponse<String?>(ex.message, ERROR_TIMESHEET_UPDATE),
                    HttpStatus.UNPROCESSABLE_ENTITY
            )
        }
    }


}
