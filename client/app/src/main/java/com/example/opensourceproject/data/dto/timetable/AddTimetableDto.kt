package com.example.opensourceproject.data.dto.timetable

data class AddTimetableDto(
    val subject: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String
)
