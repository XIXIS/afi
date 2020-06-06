package com.afi.billablehours.repositories

import com.afi.billablehours.models.Invoice
import org.springframework.data.repository.PagingAndSortingRepository

interface InvoiceRepository : PagingAndSortingRepository<Invoice?, Long?> {


}