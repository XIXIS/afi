package com.afi.billablehours.repositories

import com.afi.billablehours.models.Grade
import org.springframework.data.repository.PagingAndSortingRepository

interface GradeRepository : PagingAndSortingRepository<Grade?, Long?> {


}