package com.example.opensourceproject.view.schedule


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.opensourceproject.data.dto.schedule.ModificationRequest
import com.example.opensourceproject.data.scheduleModel.ScheduleRepository
import com.example.opensourceproject.data.scheduleModel.ScheduleViewModel
import com.example.opensourceproject.service.RetrofitClient
import java.time.LocalDate

@Composable
fun ModificationScreen(
    navController: NavController, viewModel: ScheduleViewModel
) {
    val modificationRequest = viewModel.modificationRequest.value
    val context = LocalContext.current

    var title by remember { mutableStateOf(modificationRequest?.title ?: "") }
    var selectedCategory = remember { mutableStateOf(modificationRequest?.category?:"") }
    var selectedDate by remember { mutableStateOf(modificationRequest?.scheduleDate.toString()
        ?:"") }

    val categoryList = Category.entries
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var clickedButton by remember { mutableStateOf<String>("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text("일정 수정", fontSize = 24.sp,
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
            DropdownMenu(expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()) {
                categoryList.forEach { category ->
                    DropdownMenuItem(text = { Text(text = category.name
                        , fontWeight = FontWeight.Bold) },
                        onClick = {
                            selectedCategory.value = category.name
                            expanded = false
                        })
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
                    clickedButton = "modification"
                    if((title.isEmpty() || selectedCategory.value.isEmpty() ||selectedDate =="Select Date")) {
                        Toast.makeText(context, "모든 내용을 다 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val date:LocalDate= convertToLocalDate(selectedDate)
                        Log.d("date data", "selectedDate: $selectedDate")

                        val modificationData = ModificationRequest(title, selectedCategory.value, date)
                        if (modificationRequest != null) {
                            viewModel.modifySchedule(
                                modificationRequest.id, modificationData)
                        }
                        if(!viewModel.toastMessage.value.isNullOrBlank()){
                            Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_SHORT).show()
                            viewModel.toastMessage.value = null
                        }
                        navController.navigate("ScheduleView")
                    }
                },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (clickedButton == "modification") colorResource(id = R.color.Yellow) else Color.White
                )
            ) {
                Text("수정", color = Color.Black)
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


@Preview(showBackground = true)
@Composable
fun ModificationPreview() {
    ModificationScreen(
        navController = rememberNavController(), viewModel = ScheduleViewModel(
        ScheduleRepository(RetrofitClient.getInstance(context = LocalContext.current )))
        )
}
