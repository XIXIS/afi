package com.afi.billablehours.models

import com.afi.billablehours.config.Auditable
import lombok.NonNull
import lombok.ToString
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@ToString
@Table(name = "timesheets")
class TimeSheet() : Auditable() {

    constructor(user: User?, rate: Double?, company: Company?, date: LocalDate?, startTime: LocalTime?, endTime: LocalTime?) : this() {
        this.user = user
        this.company = company
        this.date = date
        this.startTime = startTime
        this.endTime = endTime
        this.rate = rate
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @NonNull
    @OneToOne
    var user: User? = null

    @NonNull
    var rate: Double? = null

    @NonNull
    @OneToOne
    var company: Company? = null

    @NonNull
    @Column(nullable = false)
    var date: LocalDate? = null

    @NonNull
    @Column(nullable = false)
    var startTime: LocalTime? = null

    @NonNull
    @Column(nullable = false)
    var endTime: LocalTime? = null

    @NonNull
    @Column(nullable = false)
    var invoiced: Boolean? = false
}