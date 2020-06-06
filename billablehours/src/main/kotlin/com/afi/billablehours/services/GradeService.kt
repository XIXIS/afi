package com.afi.billablehours.services

import com.afi.billablehours.models.APIResponse
import com.afi.billablehours.models.Grade
import com.afi.billablehours.models.requests.CreateGradeRequest
import com.afi.billablehours.repositories.GradeRepository
import com.github.slugify.Slugify
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.*


@Service
class GradeService(private val gradeRepository: GradeRepository) : Serializable {

    fun listAll(pageable: Pageable): Page<Grade?> {
        return gradeRepository.findAll(pageable)
    }

    fun listAll(): Iterable<Grade?> {
        return gradeRepository.findAll()
    }

    fun findById(id: Long): Optional<Grade?> {
        return gradeRepository.findById(id)
    }

    fun update(request: CreateGradeRequest, grade: Grade): ResponseEntity<*>? {
        grade.name = request.name
        grade.alias = Slugify().withUnderscoreSeparator(true).slugify(request.name).toUpperCase()
        grade.rate = request.rate

        return ResponseEntity<Any?>(
                APIResponse(save(grade), "Grade successfully updated"),
                HttpStatus.OK
        )
    }

    fun save(company: Grade): Grade {
        return gradeRepository.save(company)
    }

    fun create(request: CreateGradeRequest): Grade {
        val alias: String = Slugify().withUnderscoreSeparator(true).slugify(request.name).toUpperCase()
        val grade = Grade(request.name, alias, request.rate)
        return save(grade)
    }


}
