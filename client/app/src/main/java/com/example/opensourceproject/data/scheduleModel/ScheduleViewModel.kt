package com.example.opensourceproject.data.scheduleModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opensourceproject.data.dto.schedule.AddScheduleRequest
import com.example.opensourceproject.data.dto.schedule.ModificationRequest
import com.example.opensourceproject.data.dto.schedule.ScheduleRequest
import kotlinx.coroutines.launch

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {

    var scheduleList = mutableStateListOf<ScheduleRequest>()
    var modificationRequest = mutableStateOf<ScheduleRequest?>(null)
    var toastMessage = mutableStateOf<String?>(null)

    fun fetchSchedule(year: Int, month: Int) {
        viewModelScope.launch {
            val result = repository.getMonthList(year, month)
            if(result.isSuccessful)
            {
                val response: List<ScheduleRequest> = result.body() ?: emptyList()
                scheduleList.clear()
                scheduleList.addAll(response)
            }
            else {
                toastMessage.value = "일정 불러오기 실패"
            }

        }
    }
    fun getScheduleListByEmail(list :MutableList<ScheduleRequest>){
        viewModelScope.launch {
            val response = repository.getScheduleByEmail()
            if (response != null) {
                if(response.isEmpty()){
                 list.clear()
                }
                else{
                    list.addAll(response)
                }
                Log.d("api", "api success!")
            }
            else {
                list.clear()
                Log.e("ScheduleAPI", "api fail")
                toastMessage.value = "오늘 일정 불러오기 실패"
            }
        }
    }

    fun addSchedule(request: AddScheduleRequest) {
        viewModelScope.launch {
            val result = repository.addSchedule(request)
            if (result != null) {
                Log.d("api", "api success!")
            } else {
                toastMessage.value = "일정 추가 실패"
                Log.e("ScheduleAPI", "api fail")
            }
        }
    }

    fun modifySchedule(id: Long, request: ModificationRequest) {
        viewModelScope.launch {
            val result = repository.modifySchedule(id, request)
            if(result == null)
                toastMessage.value = "일정 수정 실패"
         }
    }

    fun deleteSchedule(id: Long) {
        viewModelScope.launch {
           val success = repository.deleteSchedule(id)
            if(success)
            {
                scheduleList.removeIf { it.id == id }
            }
            else{
                toastMessage.value = "일정 삭제 실패."
            }

        }
    }


}