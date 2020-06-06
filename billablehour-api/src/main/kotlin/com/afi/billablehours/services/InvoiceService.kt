package com.afi.billablehours.services

//import com.google.common.collect.Lists
import com.afi.billablehours.models.Client
import com.afi.billablehours.models.Invoice
import com.afi.billablehours.models.TimeSheet
import com.afi.billablehours.models.requests.CreateInvoiceRequest
import com.afi.billablehours.repositories.InvoiceRepository
import com.afi.billablehours.utils.Constants.Companion.ERROR_CLIENT_NOT_FOUND
import com.afi.billablehours.utils.Constants.Companion.ERROR_NO_TIMESHEETS
import com.afi.billablehours.utils.exceptions.ClientNotFoundException
import com.afi.billablehours.utils.exceptions.TimesheetInvoicedException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


@Service
class InvoiceService(private val invoiceRepository: InvoiceRepository, private val clientService: ClientService,
                     private val timeSheetService: TimeSheetService) : Serializable {

    fun listAll(pageable: Pageable): Page<Invoice?> {
        return invoiceRepository.findAll(pageable)

    }

    fun listAll(): Iterable<Invoice?> {
        return invoiceRepository.findAll()
    }

    fun save(invoice: Invoice): Invoice {
        return invoiceRepository.save(invoice)
    }


    fun create(request: CreateInvoiceRequest): Invoice {

        val client: Optional<Client?> = clientService.findById(request.clientId)
        if (!client.isPresent) {
            throw ClientNotFoundException(ERROR_CLIENT_NOT_FOUND(request.clientId))
        }

//        val timeSheets : List<TimeSheet> = Lists.newArrayList((timeSheetService.findByClientId(client.get().id))
        // get timesheets for client and compute total cost
        val clientTimeSheets : Iterable<TimeSheet?> = timeSheetService.findByClientIdNotInvoiced(client.get().id)

        if(clientTimeSheets.count() == 0){
            throw TimesheetInvoicedException(ERROR_NO_TIMESHEETS)
        }

        val timeSheets : ArrayList<TimeSheet> = ArrayList()
        var totalCost = 0.0
        for(timeSheet in clientTimeSheets){
            timeSheet?.invoiced = true
            timeSheetService.save(timeSheet!!)
            timeSheets.add(timeSheet)
            totalCost+=(timeSheet.cost!!)
        }

        val invoice = Invoice(client.get(), timeSheets, totalCost)
        return save(invoice)
    }

    fun findById(id: Long): Optional<Invoice?> {
        return invoiceRepository.findById(id)
    }



}
