package com.example.opensourceproject.view.timeTable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opensourceproject.data.dto.timetable.AddTimetableDto
import com.example.opensourceproject.data.dto.timetable.GetTimetableDto
import com.example.opensourceproject.data.timeTableModel.TimetableViewModel

@Composable
fun AddSubjectDialog(
    onDismiss: () -> Unit,
    viewModel: TimetableViewModel
) {
    val context = LocalContext.current

    var subjectName by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var selectedHour by remember { mutableStateOf(9) }
    var selectedMinute by remember { mutableStateOf(0) }
    var selectedEndHour by remember { mutableStateOf(9) }
    var selectedEndMinute by remember { mutableStateOf(0) }

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")

    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(20.dp)
            .width(320.dp)
    ) {
        Column {
            Text("새 과목 추가", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)

            Spacer(modifier = Modifier.height(10.dp))

            // 과목명 입력 필드
            OutlinedTextField(
                value = subjectName,
                onValueChange = { subjectName = it },
                label = { Text("과목명", color = Color.White) },
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))


            Box {
                OutlinedTextField(
                    value = selectedDay,
                    onValueChange = { },
                    label = { Text("요일 선택", color = Color.White) },
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "요일 선택", tint = Color.White)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    days.forEach { day ->
                        DropdownMenuItem(onClick = {
                            selectedDay = day
                            expanded = false
                        }) {
                            Text(day)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            //  시작 시간 선택
            Text("시작 시간", fontWeight = FontWeight.Bold, color = Color.White)
            TimeSelector(
                selectedHour = selectedHour,
                selectedMinute = selectedMinute,
                onHourChange = { selectedHour = it },
                onMinuteChange = { selectedMinute = it }
                ,false
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 종료 시간 선택
            Text("종료 시간", fontWeight = FontWeight.Bold, color = Color.White)
            TimeSelector(
                selectedHour = selectedEndHour,
                selectedMinute = selectedEndMinute,
                onHourChange = { selectedEndHour = it },
                onMinuteChange = { selectedEndMinute = it }
                ,false
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red) ) {
                    Text("취소")
                }
                Button(onClick = {
                    // 모든 필수 항목 입력 체크
                    if (subjectName.isBlank() || selectedDay.isBlank()) {
                        Toast.makeText(context, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    //  시작 시간이 종료시간보다 크거나 같으면 안됨
                    val startTimeString = convertToTimeString(selectedHour, selectedMinute)
                    val endTimeString = convertToTimeString(selectedEndHour, selectedEndMinute)
                    if (startTimeString >= endTimeString) {
                        Toast.makeText(context, "시간을 다시 입력해주세요", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val newSchedule = AddTimetableDto(subjectName, selectedDay, startTimeString, endTimeString)

                    //  기존 일정과 시간이 겹치는지 확인
                    val existingSchedules = viewModel.timetableList // 기존 일정 가져오기
                    if (isTimeConflict(existingSchedules, newSchedule)) {
                        Toast.makeText(context, "이미 겹치는 시간이 있습니다!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    //  중복이 없으면 정상적으로 추가
                    viewModel.addTimetable(newSchedule) {
                        onDismiss()
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green)) {
                    Text("추가")
                }
            }
        }
    }
}

//  시간 중복 체크 함수
fun isTimeConflict(
    existingSchedules: List<GetTimetableDto>,
    newSchedule: AddTimetableDto
): Boolean {
    for (schedule in existingSchedules) {
        if (schedule.dayOfWeek == newSchedule.dayOfWeek) {
            val existingStart = schedule.startTime.split(":").map { it.toInt() }
            val existingEnd = schedule.endTime.split(":").map { it.toInt() }
            val newStart = newSchedule.startTime.split(":").map { it.toInt() }
            val newEnd = newSchedule.endTime.split(":").map { it.toInt() }

            val existingStartMinutes = existingStart[0] * 60 + existingStart[1]
            val existingEndMinutes = existingEnd[0] * 60 + existingEnd[1]
            val newStartMinutes = newStart[0] * 60 + newStart[1]
            val newEndMinutes = newEnd[0] * 60 + newEnd[1]

            //  시간이 겹치는 경우 (추가 불가)
            if (!(newEndMinutes <= existingStartMinutes || newStartMinutes >= existingEndMinutes)) {
                return true
            }
        }
    }
    return false
}

//  시간 변환 함수
fun convertToTimeString(hour: Int, minute: Int): String {
    return String.format("%02d:%02d:00", hour, minute)
}

@Composable
fun TimeSelector(
    selectedHour: Int,
    selectedMinute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    isEditMode: Boolean
) {
    val hours = (9..20).toList()
    val minutes = (0..59).toList()


    val hourListState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (isEditMode) hours.indexOf(selectedHour) else 0
    )
    val minuteListState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (isEditMode) minutes.indexOf(selectedMinute) else 0
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "시", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            LazyColumn(
                state = hourListState,
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
            ) {
                items(hours.size) { index ->
                    val hour = hours[index]
                    Text(
                        text = hour.toString(),
                        fontSize = 20.sp,
                        fontWeight = if (hour == selectedHour) FontWeight.Bold else FontWeight.Normal,
                        color = if (hour == selectedHour) Color.Yellow else Color.White,
                        modifier = Modifier
                            .padding(4.dp)
                            .background(if (hour == selectedHour) Color.DarkGray else Color.Transparent)
                            .fillMaxWidth()
                            .clickable {
                                onHourChange(hour)
                            }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "분", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            LazyColumn(
                state = minuteListState,
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
            ) {
                items(minutes.size) { index ->
                    val minute = minutes[index]
                    Text(
                        text = "%02d".format(minute),
                        fontSize = 20.sp,
                        fontWeight = if (minute == selectedMinute) FontWeight.Bold else FontWeight.Normal,
                        color = if (minute == selectedMinute) Color.Yellow else Color.White,
                        modifier = Modifier
                            .padding(4.dp)
                            .background(if (minute == selectedMinute) Color.DarkGray else Color.Transparent)
                            .fillMaxWidth()
                            .clickable {
                                onMinuteChange(minute)
                            }
                    )
                }
            }
        }
    }
}
