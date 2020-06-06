package com.afi.billablehours.models.requests

import org.springframework.validation.annotation.Validated
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import javax.validation.constraints.NotNull


@Validated
data class CreateTimeSheetEntryRequest(@NotNull val clientId: Long, @NotNull val date: LocalDate,
                                       @NotNull val startTime: LocalTime, @NotNull val endTime: LocalTime) : Serializable

