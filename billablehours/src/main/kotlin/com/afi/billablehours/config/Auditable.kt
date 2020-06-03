package com.afi.billablehours.config

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Auditable

@CreatedBy
private var createdBy: Long? = null

@CreatedDate
private val creationDate: LocalDateTime? = null

@LastModifiedBy
private val lastModifiedBy: Long? = null

@LastModifiedDate
private val lastModifiedDate: LocalDateTime? = null


