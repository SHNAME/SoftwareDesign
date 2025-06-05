package com.example.opensourceproject.view.timeTable

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.opensourceproject.data.dto.timetable.AddTimetableDto
import com.example.opensourceproject.data.dto.timetable.GetTimetableDto
import com.example.opensourceproject.data.timeTableModel.TimetableRepository
import com.example.opensourceproject.data.timeTableModel.TimetableViewModel
import com.example.opensourceproject.service.RetrofitClient
import com.example.opensourceproject.view.bar.BottomBar
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun TimeTableScreen(viewModel: TimetableViewModel, navController: NavController) {
    val context = LocalContext.current
    val timeTableList by remember { derivedStateOf { viewModel.timetableList } }
    var selectedSubject by remember { mutableStateOf<GetTimetableDto?>(null) }
    var bottomBar by remember {
        mutableStateOf(true)
    }

    //  다이얼로그 표시 여부 상태 변수
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchTimetable()
        if(!viewModel.toastMessage.value.isNullOrBlank())
        {
            Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_LONG).show()
            viewModel.toastMessage.value = null
        }
    }


    Scaffold(
        bottomBar = {
            if(bottomBar){
                BottomBar(navController, "tlgud119@naver.com")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "시간표",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        viewModel.deleteAllTimetable()
                        if(!viewModel.toastMessage.value.isNullOrBlank()){
                            Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_LONG).show()
                            viewModel.toastMessage.value = null
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "전체 삭제", tint = Color.White, modifier = Modifier.size(30.dp))
                    }


                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "시간표 추가", tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("월", "화", "수", "목", "금").forEach { day ->
                    Text(
                        text = day,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            //  시간표 본문
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items((9..21).toList()) { hour ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "$hour",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.width(25.dp),
                            textAlign = TextAlign.Center
                        )


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            listOf("월", "화", "수", "목", "금").forEach { day ->
                                val classData = timeTableList.find { isClassAtHour(it, hour, day) }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp)
                                        .height(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TimeTableCell(classData){ selectedSubject = it;
                                    bottomBar = false}
                                    }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }

        //  시간표 추가 다이얼로그
        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)), // 기존 화면 흐리게 처리
                contentAlignment = Alignment.Center
            ) {
                AddSubjectDialog(
                    onDismiss = { showDialog = false },
                    viewModel
                )
            }
        }
        //시간표 추가에 실패한 경우 메세지를 시간표 화면에서 보여줌
        if(!viewModel.toastMessage.value.isNullOrBlank())
        {
            Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_LONG).show()
            viewModel.toastMessage.value = null
        }
        if (selectedSubject != null) {
            EditSubjectDialog(viewModel=viewModel
                ,
                subject = selectedSubject!!,
                onDismiss = {   selectedSubject = null
                    bottomBar = true },
                onEdit = {
                    selectedSubject =null
                         bottomBar = true},
                onDelete = {
                    viewModel.deleteTimetable(selectedSubject!!.id)
                    selectedSubject =null
                    bottomBar=true
                    if(!viewModel.toastMessage.value.isNullOrBlank()){
                        Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_LONG).show()
                        viewModel.toastMessage.value = null
                    }
                }
            )
        }
    }
}

//  특정 시간이 수업 시간에 포함되는지 확인
fun isClassAtHour(timeTable: GetTimetableDto, hour: Int, day: String): Boolean {
    val days = timeTable.dayOfWeek.split(",") // "Mon,Tue,Wed" → ["Mon", "Tue", "Wed"]
    val mappedDay = mapKoreanDayToEnglish(day) // "월" → "Mon"

    val startTime = getParsedStartTime(timeTable.startTime)
    val endTime = getParsedEndTime(timeTable.endTime)

    val isDayMatched = mappedDay in days
    val isTimeMatched = hour in startTime.hour..endTime.hour

    Log.d(
        "TimeTableCheck",
        "Day: $day ($mappedDay) vs Server: ${timeTable.dayOfWeek}, " +
                "Hour: $hour, Start: ${startTime.hour}:${startTime.minute}, End: ${endTime.hour}:${endTime.minute}, Matched: ${isDayMatched && isTimeMatched}"
    )

    return isDayMatched && isTimeMatched
}

//  한글 요일 → 영어 요일 변환
fun mapKoreanDayToEnglish(koreanDay: String): String {
    return when (koreanDay) {
        "월" -> "Mon"
        "화" -> "Tue"
        "수" -> "Wed"
        "목" -> "Thu"
        "금" -> "Fri"
        else -> ""
    }
}


fun getParsedStartTime(startTime: String): LocalTime {
    return LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
}

fun getParsedEndTime(endTime: String): LocalTime {
    return LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
}

// 시간을 "HH:mm:ss" → "9시 30분" 형식으로 변환
fun formatTime(time: String): String {
    val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"))
    return "${localTime.hour}시 ${if (localTime.minute > 0) "${localTime.minute}분" else ""}".trim()
}

@Composable
fun TimeTableCell(timeTable: GetTimetableDto?, onClick: (GetTimetableDto) -> Unit) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(if (timeTable != null) 60.dp else 40.dp)
            .background(if (timeTable != null) Color.Yellow else Color.DarkGray)
            .padding(5.dp)
            .clickable { timeTable?.let { onClick(it) } },
        contentAlignment = Alignment.Center
    ) {
        if (timeTable != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = timeTable.subjectName, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = "${formatTime(timeTable.startTime)} ~ ${formatTime(timeTable.endTime)}", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
            }
        }
    }
}
@Composable
fun EditSubjectDialog(
    viewModel: TimetableViewModel,
    subject: GetTimetableDto,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var subjectName by remember { mutableStateOf(subject.subjectName) }
    var selectedDay by remember { mutableStateOf(subject.dayOfWeek) }
    var expanded by remember { mutableStateOf(false) }

    //  기존 저장된 시간을 파싱하여 시간과 분을 분리
    val (startHour, startMinute) = subject.startTime.split(":").map { it.toInt() }
    val (endHour, endMinute) = subject.endTime.split(":").map { it.toInt() }

    var selectedStartHour by remember { mutableStateOf(startHour) }
    var selectedStartMinute by remember { mutableStateOf(startMinute) }
    var selectedEndHour by remember { mutableStateOf(endHour) }
    var selectedEndMinute by remember { mutableStateOf(endMinute) }

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = subjectName,
                onValueChange = { subjectName = it },
                label = { Text("과목명", color = Color.Black) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Yellow,
                    focusedContainerColor = Color.Yellow,
                    unfocusedContainerColor = Color.Green
                )
            )

            Spacer(modifier = Modifier.height(10.dp))


            Text("시작 시간", fontWeight = FontWeight.Bold, color = Color.White)
            TimeSelector(
                selectedHour = selectedStartHour,
                selectedMinute = selectedStartMinute,
                onHourChange = { selectedStartHour = it },
                onMinuteChange = { selectedStartMinute = it }
                ,true
            )

            Spacer(modifier = Modifier.height(10.dp))


            Text("종료 시간", fontWeight = FontWeight.Bold, color = Color.White)
            TimeSelector(
                selectedHour = selectedEndHour,
                selectedMinute = selectedEndMinute,
                onHourChange = { selectedEndHour = it },
                onMinuteChange = { selectedEndMinute = it }
                ,true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box {
                OutlinedTextField(
                    value = selectedDay,
                    onValueChange = {}, // 직접 입력 불가
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    if (subjectName.isBlank() || selectedDay.isBlank()) {
                        Toast.makeText(context, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val startTimeString = convertToTimeString(selectedStartHour, selectedStartMinute)
                    val endTimeString = convertToTimeString(selectedEndHour, selectedEndMinute)

                    if(startTimeString >= endTimeString) {
                        Toast.makeText(context, "시간을 다시 입력해주세요", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val newSchedule = AddTimetableDto(subjectName, selectedDay, startTimeString, endTimeString)

                    val existingSchedules = viewModel.timetableList
                    if (isTimeConflict(existingSchedules, newSchedule)) {
                        Toast.makeText(context, "이미 겹치는 시간이 있습니다!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    viewModel.modifyTimetable(subject.id, newSchedule) { onEdit() }
                    if(!viewModel.toastMessage.value.isNullOrBlank()){
                        Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_LONG).show()
                        viewModel.toastMessage.value = null
                    }

                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)) {
                    Text("수정하기", fontWeight = FontWeight.Bold)
                }

                Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("삭제", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { onDismiss() }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                Text("닫기", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun TimeTableScreenPreview() {
    val viewModel = TimetableViewModel(TimetableRepository(RetrofitClient.getInstance(context = LocalContext.current)))

    viewModel.timetableList.add(
        GetTimetableDto(
            id = 1L,
            email = "tlgud119@naver.com",
            subjectName = "운영체제",
            dayOfWeek = "Mon",
            startTime = "09:45:00",
            endTime = "10:20:00"
        )
    )

    TimeTableScreen(
        viewModel,
        navController = rememberNavController()
    )
}