package com.example.opensourceproject.data.scheduleModel

import android.util.Log
import com.example.opensourceproject.data.dto.schedule.AddScheduleRequest
import com.example.opensourceproject.data.dto.schedule.ModificationRequest
import com.example.opensourceproject.data.dto.schedule.ScheduleRequest
import com.example.opensourceproject.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ScheduleRepository(private val api: ApiService) {

    suspend fun getMonthList(year: Int, month: Int): Response<List<ScheduleRequest>> {
        return withContext(Dispatchers.IO) {
            api.getMonthList(year, month)
        }
    }

    suspend fun addSchedule(request: AddScheduleRequest): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.addSchedule(request).execute()
                if (response.isSuccessful) {
                    Log.d("ScheduleAPI", "Test success: ${response.body()}")
                    response.body()
                } else {
                    Log.e("ScheduleAPI", "Test fail: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("ScheduleAPI", "NetworkError: ${e.message}")
                null
            }
        }
    }


    suspend fun modifySchedule(id: Long, request: ModificationRequest): String? {

        return withContext(Dispatchers.IO) {
            try {
                val response = api.modifySchedule(id, request)
                if (response.isSuccessful) {
                    Log.d("ScheduleAPI", "Schedule correction success: ${response.body()}")
                    response.body()
                } else {
                    Log.e("ScheduleAPI", "Schedule correction fail: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("ScheduleAPI", "NetworkError: ${e.message}")
                null
            }
        }
    }
    suspend fun getScheduleByEmail(): List<ScheduleRequest>?
    {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getListByUserEmail()
                if (response.isSuccessful) {
                    Log.d("ScheduleAPI", "Get List Success: ${response.body()}")
                    if(response.body() ==null){
                        emptyList<ScheduleRequest>()
                    }
                    else{
                        response.body()
                    }
                } else {
                    Log.e("ScheduleAPI", "Get List Fail: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("ScheduleAPI", "Network Error: ${e.message}")
                null
            }
        }
    }

    suspend fun deleteSchedule(id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.deleteSchedule(id).execute()
                if (response.isSuccessful) {
                    Log.d("ScheduleAPI", "Schedule Delete success")
                    response.body() ?: true
                } else {
                    Log.e("ScheduleAPI", "Schedule Delete fail: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("ScheduleAPI", "Network Error: ${e.message}")
                false
            }
        }
    }


}
