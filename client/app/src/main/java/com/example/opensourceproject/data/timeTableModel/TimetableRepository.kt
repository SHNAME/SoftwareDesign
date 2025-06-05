package com.example.opensourceproject.data.timeTableModel

import android.util.Log
import com.example.opensourceproject.data.dto.timetable.AddTimetableDto
import com.example.opensourceproject.data.dto.timetable.GetTimetableDto
import com.example.opensourceproject.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TimetableRepository(private val api: ApiService) {

    //  시간표 조회
    suspend fun getTimetable(): List<GetTimetableDto>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getTimeTable()
                if (response.isSuccessful) {
                    Log.d("TimeTableAPI", "Get TimeTable Success: ${response.body()}")
                    response.body()
                } else {
                    Log.e("TimeTableAPI", "Get TimeTable fail: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("TimeTableAPI", "NetWork Error: ${e.message}")
                null
            }
        }
    }

    //  시간표 추가
    suspend fun addTimetable(request: AddTimetableDto): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.addTimeTable(request)
                if (response.isSuccessful) {
                    Log.d("TimeTableAPI", "TimeTable addition success")
                    true
                } else {
                    Log.e("TimeTableAPI", "TimeTable addition fail: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("TimeTableAPI", " NetWork Error: ${e.message}")
                false
            }
        }
    }

    // 시간표 수정
    suspend fun modifyTimetable(id: Long, request: AddTimetableDto): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.modifyTimeTable(id, request)
                if (response.isSuccessful) {
                    Log.d("TimeTableAPI", "TimeTable correction Success")
                    true
                } else {
                    Log.e("TimeTableAPI", "TimeTable correction fail: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("TimeTableAPI", "NetWork Error: ${e.message}")
                false
            }
        }
    }

    //  특정 시간표 삭제
    suspend fun deleteTimetable(id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.deleteTimeTable(id)
                if (response.isSuccessful) {
                    Log.d("TimeTableAPI", "TimeTable Delete Success")
                    true
                } else {
                    Log.e("TimeTableAPI", "TimeTable Delete fail: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("TimeTableAPI", "NetWork Error: ${e.message}")
                false
            }
        }
    }

    //  전체 시간표 삭제
    suspend fun deleteAllTimetable(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.deleteAllTimeTable()
                if (response.isSuccessful) {
                    Log.d("TimeTableAPI", "TimeTable Delete Success")
                    true
                } else {
                    Log.e("TimeTableAPI", "TimeTable Delete fail: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("TimeTableAPI", "NetWork Error: ${e.message}")
                false
            }
        }
    }
}
