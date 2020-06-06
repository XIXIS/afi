package com.afi.billablehours.repositories

import com.afi.billablehours.models.TimeSheet
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface TimeSheetRepository : PagingAndSortingRepository<TimeSheet?, Long?> {

    fun findAllByUserId(userId: Long?, pageable: Pageable): Page<TimeSheet?>
    fun findAllByUserId(userId: Long?): List<TimeSheet?>


}