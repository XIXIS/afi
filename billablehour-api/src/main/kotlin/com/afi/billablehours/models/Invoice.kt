package com.afi.billablehours.models

import com.afi.billablehours.config.Auditable
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "invoices")
class Invoice() : Auditable() {

    constructor(client: Client?, timesheets: List<TimeSheet>, totalCost: Double?): this(){
        this.client = client
        this.timesheets = timesheets
        this.totalCost = totalCost
    }

    @Id
    @GeneratedValue
    var id: Long? = null

    @NotNull
    @OneToOne
    var client: Client? = null

    @NotNull
    @OneToMany
    var timesheets: List<TimeSheet?> = ArrayList()

    @NotNull
    @Column(nullable = false)
    var totalCost: Double? = null


}