package com.example.opensourceproject.data.dto.schedule

import java.time.LocalDate

data class ModificationRequest(
    val title: String,
    val category: String,
    val schedule: LocalDate,

)
