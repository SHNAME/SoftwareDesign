package com.example.opensourceproject.view.schedule

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.opensourceproject.data.scheduleModel.ScheduleRepository
import com.example.opensourceproject.data.scheduleModel.ScheduleViewModel
import com.example.opensourceproject.service.RetrofitClient
import com.example.opensourceproject.view.bar.BottomBar
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel, navController: NavController
) {

    var selectedDate by remember { mutableStateOf(LocalDate.now(ZoneId.of("Asia/Seoul"))) }
    val context = LocalContext.current

    var showDatePicker by remember { mutableStateOf(false) }

    // LazyRow의 스크롤 상태 저장
    val listState = rememberLazyListState()



    LaunchedEffect(selectedDate) {
        viewModel.fetchSchedule(selectedDate.year, selectedDate.monthValue)
        if(!viewModel.toastMessage.value.isNullOrBlank()){
            Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_LONG).show()
            viewModel.toastMessage.value = null
        }

        // 새로운 날짜가 선택되면 해당 날짜로 스크롤 이동
        val index = selectedDate.dayOfMonth - 1
        listState.animateScrollToItem(index)
    }

    val schedules = viewModel.scheduleList
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val findSchedules = schedules.filter {
        val parsedDate = LocalDate.parse(it.scheduleDate.toString(), formatter)
        parsedDate == selectedDate
    }

    Log.d("ScheduleFilter", "Select Date: $selectedDate")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "${selectedDate.dayOfMonth} ",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.Yellow,
                    fontSize = 40.sp
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "${selectedDate.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)} ",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 20.sp,
                    color = Color.White
                )

                Text(
                    text = "${selectedDate.year} ",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 20.sp,
                    color = Color.White
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)}",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 17.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )


                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "날짜 선택",
                        tint = Color.White
                    )
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate("AddSchedule") },
                containerColor = Color(0xFF4CAF50),
                modifier = Modifier.padding(end = 15.dp, top = 15.dp)
            ) {
                Text("+", fontSize = 30.sp, color = Color.White)
            }
        }


        if (showDatePicker) {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                    selectedDate = newDate
                    showDatePicker = false
                },
                selectedDate.year,
                selectedDate.monthValue - 1,
                selectedDate.dayOfMonth
            ).show()
        }


        MonthlyDateSelector(selectedDate, listState) { newDate ->
            selectedDate = newDate
        }

        if (findSchedules.isEmpty()) {
            Text(
                text = "오늘 일정 없음",
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(findSchedules.size) { index ->
                    ScheduleItem(findSchedules[index], viewModel, navController,context)
                }
            }
        }

        BottomBar(navController, "tlgud119")
    }
}


@Composable
fun MonthlyDateSelector(selectedDate: LocalDate, listState: LazyListState, onDateSelected: (LocalDate) -> Unit) {
    val firstDayOfMonth = selectedDate.withDayOfMonth(1)


    val daysOfMonth = (0 until selectedDate.lengthOfMonth()).map { firstDayOfMonth.plusDays(it.toLong()) }

    LaunchedEffect(selectedDate) {
        val index = selectedDate.dayOfMonth - 1
        listState.animateScrollToItem(index)
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(daysOfMonth) { date ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onDateSelected(date) }
                    .background(if (date == selectedDate) Color(0xFFFF9800) else Color.Gray)
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    ScheduleScreen(
        viewModel = ScheduleViewModel(ScheduleRepository(RetrofitClient.getInstance(context = LocalContext.current))),
        navController = rememberNavController(),
    )
}
