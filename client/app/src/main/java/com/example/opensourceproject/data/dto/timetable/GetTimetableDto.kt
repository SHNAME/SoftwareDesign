package com.example.opensourceproject.data.dto.timetable

data class GetTimetableDto(

    val id: Long,
    val email: String,
    val subjectName: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String

)

