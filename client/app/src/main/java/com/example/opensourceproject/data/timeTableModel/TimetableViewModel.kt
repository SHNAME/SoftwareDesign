package com.example.opensourceproject.data.timeTableModel


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.opensourceproject.data.dto.timetable.AddTimetableDto
import com.example.opensourceproject.data.dto.timetable.GetTimetableDto

import kotlinx.coroutines.launch

class TimetableViewModel(private val repository: TimetableRepository) : ViewModel() {
    var timetableList = mutableStateListOf<GetTimetableDto>()
    var toastMessage = mutableStateOf<String?>(null)

    //  시간표 조회
    fun fetchTimetable() {
        viewModelScope.launch {
            val result = repository.getTimetable()
            if (result != null) {
                timetableList.clear()
                timetableList.addAll(result)
            }
            else{
                toastMessage.value = "시간표 조회 실패 "
            }
        }
    }

    //  시간표 추가
    fun addTimetable(request: AddTimetableDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (repository.addTimetable(request)) {
                fetchTimetable() // 데이터 다시 로드
                onSuccess()
            }
            else{
                toastMessage.value = "시간표 추가 실패"
                onSuccess()
            }
        }
    }

    // 시간표 수정
    fun modifyTimetable(id: Long, request: AddTimetableDto, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (repository.modifyTimetable(id, request)) {
                timetableList.clear()
                fetchTimetable()
                onSuccess()
            }
            else{
                toastMessage.value = "시간표 수정 실패"
            }
        }
    }

    //  특정 시간표 삭제
    fun deleteTimetable(id: Long ) {
        viewModelScope.launch {
            if (repository.deleteTimetable(id)) {
                timetableList.clear()
                fetchTimetable()
            }
            else{
                toastMessage.value = "시간표 삭제 실패"
            }
        }
    }

    //  전체 시간표 삭제
    fun deleteAllTimetable() {
        viewModelScope.launch {
            if (repository.deleteAllTimetable()) {
                timetableList.clear()
                fetchTimetable()
            }
            else{
                toastMessage.value = "시간표 삭제 실패"
            }
        }
    }
}
