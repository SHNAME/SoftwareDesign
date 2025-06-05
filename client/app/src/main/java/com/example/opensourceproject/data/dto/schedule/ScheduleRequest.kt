package com.example.opensourceproject.data.dto.schedule

import java.time.LocalDate

data class ScheduleRequest (
    val id:Long,
    val title:String,
    val category:String,
    val scheduleDate:LocalDate)
