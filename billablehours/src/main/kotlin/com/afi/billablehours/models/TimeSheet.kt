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

    constructor(user: User?, company: Company?, date: LocalDate?, startTime: LocalTime?, endTime: LocalTime?): this(){
        this.user = user
        this.company = company
        this.date = date
        this.startTime = startTime
        this.endTime = endTime
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @NonNull
    @Column(nullable = false)
    @OneToOne
    var user: User? = null

    @NonNull
    @Column(nullable = false)
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

}