package com.afi.billablehours.models

import lombok.*
import java.io.Serializable
import javax.persistence.*

@Entity
@RequiredArgsConstructor
@ToString
@NoArgsConstructor
@Table(name = "user_types")
class UserType() : Serializable {

    constructor(name: String):this(){
        this.name = name
    }

    @Id
    @GeneratedValue
    var id: Long = 0

    @Column(nullable = false)
    var name: String? = null

}
