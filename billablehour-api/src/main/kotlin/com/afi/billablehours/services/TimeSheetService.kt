package com.afi.billablehours.services

import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Client
import com.afi.billablehours.models.TimeSheet
import com.afi.billablehours.models.User
import com.afi.billablehours.models.requests.CreateTimeSheetEntryRequest
import com.afi.billablehours.repositories.TimeSheetRepository
import com.afi.billablehours.utils.Constants.Companion.ERROR_CLIENT_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_NON_EXISTENT_CLIENT
import com.afi.billablehours.utils.Constants.Companion.ERROR_TIMESHEET_INVOICED
import com.afi.billablehours.utils.Constants.Companion.ERROR_TIMESHEET_UPDATE
import com.afi.billablehours.utils.Constants.Companion.LAWYER_USER_TYPE_NAME
import com.afi.billablehours.utils.Constants.Companion.SUCCESS_TIMESHEET_UPDATED
import com.afi.billablehours.utils.exceptions.ClientNotFoundException
import org.joda.time.LocalTime
import org.joda.time.Period
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.*


@Service
class TimeSheetService(private val userService: UserService, private val timeSheetRepository: TimeSheetRepository,
                       private val clientService: ClientService) : Serializable {

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


    fun findByClientIdNotInvoiced(clientId: Long?): Iterable<TimeSheet?> {
        return timeSheetRepository.findAllByClientIdAndInvoicedFalse(clientId)

    }

    fun save(company: TimeSheet): TimeSheet {
        return timeSheetRepository.save(company)
    }


    fun create(request: CreateTimeSheetEntryRequest): TimeSheet {

        val client: Optional<Client?> = clientService.findById(request.clientId)
        if (!client.isPresent) {
            throw ClientNotFoundException(ERROR_CLIENT_NOT_FOUND(request.clientId))
        }

        val user: User? = userService.authUser

        //Compute total hours and total cost
        val p = Period(LocalTime(request.startTime.hour, request.startTime.minute, request.startTime.second),
                LocalTime(request.endTime.hour, request.endTime.minute, request.endTime.second))
        val hours = p.hours
        val minutes = p.minutes
        val totalHours: Double = hours.toDouble() + (minutes.toDouble()/60)
        val cost: Double = totalHours * user?.grade?.rate!!

        val timeSheet = TimeSheet(user, user.grade?.rate, client.get(), request.date, request.startTime, request.endTime,
        cost, "$hours:$minutes")
        return save(timeSheet)
    }

    fun findById(id: Long): Optional<TimeSheet?> {
        return timeSheetRepository.findById(id)
    }


    fun update(timeSheetReq: CreateTimeSheetEntryRequest, timeSheet: TimeSheet): ResponseEntity<*>? {
        if (timeSheet.client?.id != timeSheetReq.clientId) {
            val client: Optional<Client?> = clientService.findById(timeSheetReq.clientId)
            if (!client.isPresent) {
                return ResponseEntity<Any?>(
                        APIResponse<String?>(ERROR_CLIENT_NOT_FOUND(timeSheetReq.clientId), ERROR_NON_EXISTENT_CLIENT),
                        HttpStatus.NOT_FOUND
                )
            }
            timeSheet.client = client.get()
        }

        //Check for denying update on already invoice timesheets
        if (timeSheet.invoiced!!) {
            return ResponseEntity<Any?>(
                    APIResponse<String?>(ERROR_TIMESHEET_INVOICED, ERROR_TIMESHEET_UPDATE),
                    HttpStatus.NOT_FOUND
            )
        }

        timeSheet.date = timeSheetReq.date
        timeSheet.startTime = timeSheetReq.startTime
        timeSheet.endTime = timeSheetReq.endTime

        //Compute total hours and total cost
        val p = Period(LocalTime(timeSheetReq.startTime.hour, timeSheetReq.startTime.minute, timeSheetReq.startTime.second),
                LocalTime(timeSheetReq.endTime.hour, timeSheetReq.endTime.minute, timeSheetReq.endTime.second))
        val hours = p.hours
        val minutes = p.minutes
        val totalHours: Double = hours.toDouble() + (minutes.toDouble()/60)
        val cost: Double = totalHours * timeSheet.user?.grade?.rate!!

        timeSheet.cost = cost
        timeSheet.hours = "$hours:$minutes"

        return try {
            ResponseEntity<Any?>(
                    APIResponse(save(timeSheet), SUCCESS_TIMESHEET_UPDATED),
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
