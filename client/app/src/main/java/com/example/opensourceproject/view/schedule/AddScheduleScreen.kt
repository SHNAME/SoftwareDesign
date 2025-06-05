package com.example.opensourceproject.view.schedule

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.opensourceproject.R
import com.example.opensourceproject.data.Category.Category
import com.example.opensourceproject.data.dto.schedule.AddScheduleRequest
import com.example.opensourceproject.data.scheduleModel.ScheduleRepository
import com.example.opensourceproject.data.scheduleModel.ScheduleViewModel
import com.example.opensourceproject.service.RetrofitClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun AddScheduleScreen(navController: NavController,viewModel: ScheduleViewModel) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var selectedCategory = remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("Select Date") }
    val categoryList = Category.entries
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var clickedButton by remember { mutableStateOf<String>("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text("일정 추가", fontSize = 24.sp,
            color = Color.Yellow,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목", color = Color.Black) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            minLines = 1,
            maxLines = 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Green,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = colorResource(id = R.color.Yellow)
            )
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = selectedCategory.value,
                onValueChange = { selectedCategory.value = it },
                label = { Text(text = "카테고리 선택", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .background(Color.DarkGray),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                minLines = 1,
                maxLines = 1,
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Green,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.Yellow)
                )
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                categoryList.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        },
                        onClick = {
                            selectedCategory.value = category.name
                            expanded = false
                        }
                    )

                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth()){
            TextField(
                value = selectedDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Date") },
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                ,colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Green,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = colorResource(id = R.color.Yellow)
                )
            )

            if (showDatePicker) {
                DatePickerDialogComponent(context) { date ->
                    selectedDate = date
                    showDatePicker = false
                }
            }
        }


        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    clickedButton = "save"
                    if((title.isEmpty() || selectedCategory.value.isEmpty() ||selectedDate =="Select Date")) {
                        Toast.makeText(context, "모든 내용을 다 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val date:LocalDate= convertToLocalDate(selectedDate)
                        Log.d("date data", "selectedDate: $selectedDate")
                        val newSchedule = AddScheduleRequest(title, selectedCategory.value, date)
                        viewModel.addSchedule(newSchedule)
                        navController.popBackStack()
                        if(!viewModel.toastMessage.value.isNullOrBlank()){
                            Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_SHORT).show()
                            viewModel.toastMessage.value = null
                        }
                    }
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (clickedButton == "save") colorResource(id = R.color.Yellow) else Color.White
                )
            ) {
                Text("저장", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { clickedButton = "cancel"
                          navController.popBackStack()},
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (clickedButton == "cancel") Color.Gray else Color.Red
                )
            ) {
                Text("취소", color = Color.Black)
            }
        }
    }
}

@Composable
fun DatePickerDialogComponent(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
        onDateSelected(formattedDate)
    }, year, month, day).show()
}

// 날짜 문자열을 LocalDate 타입으로 변경하는 메서드
fun convertToLocalDate(dateString: String): LocalDate {
    return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
}

@Preview(showBackground = true)
@Composable
fun AddScheduleScreenPreview() {
    AddScheduleScreen(navController = rememberNavController(),viewModel= ScheduleViewModel(
        ScheduleRepository(RetrofitClient.getInstance(context = LocalContext.current ))))
}
