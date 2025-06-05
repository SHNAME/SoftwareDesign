package com.example.opensourceproject.view.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.opensourceproject.R
import com.example.opensourceproject.data.Category.Category
import com.example.opensourceproject.data.dto.schedule.ScheduleRequest
import com.example.opensourceproject.data.scheduleModel.ScheduleRepository
import com.example.opensourceproject.data.scheduleModel.ScheduleViewModel
import com.example.opensourceproject.service.RetrofitClient
import com.example.opensourceproject.view.bar.BottomBar
import com.example.opensourceproject.view.login.logout
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun MainView(email: String?, navController: NavController, context: Context,viewModel: ScheduleViewModel) {

    var selectedDate by remember { mutableStateOf(LocalDate.now(ZoneId.of("Asia/Seoul"))) }
    var schedule = remember { mutableStateListOf<ScheduleRequest>() }
    LaunchedEffect(selectedDate) {
         viewModel.getScheduleListByEmail(schedule)
        if(!viewModel.toastMessage.value.isNullOrBlank())
        {
            Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_SHORT).show()
            viewModel.toastMessage.value = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Text(
                text = "Main",
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
                ,color = Color.Yellow
                )
                Button(onClick = {
                    logout(context, navController)
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                ), modifier = Modifier.padding(end=5.dp)
                ) {
                    Text(
                        text = "Logout",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }


            }

            Text(
                text = "Categories",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 30.dp, bottom = 15.dp,start= 20.dp)
                ,color = Color.Green
            )

            CategoryLazyRow(context,schedule)

            TodayScheduleList(schedule)
        }

        BottomBar(navController, email)
    }
}

@Composable
fun CategoryLazyRow(context: Context,list: List<ScheduleRequest>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(Category.values()) { category ->
            CategoryItem(category,list)
        }
    }
}
@Composable
fun CategoryItem(category: Category, list: List<ScheduleRequest>) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.purple_200)),
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .clickable {
                showDialog = true
            }
            .size(120.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
                    ,color = Color.Black
            )

        }
    }

    if (showDialog) {
        CategoryScheduleDialog(category, list, onDismiss = { showDialog = false })
    }
}

@Composable
fun CategoryScheduleDialog(category: Category, list: List<ScheduleRequest>, onDismiss: () -> Unit) {
    val filteredList = list.filter { it.category == category.name }
        .sortedBy{ it.scheduleDate }


        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${category.name.toLowerCase(Locale.current)} Schedule", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            IconButton(onClick = onDismiss) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                            }
                        }
                        if(filteredList.isEmpty())
                        {
                            Text("일정 없음", fontWeight = FontWeight.Bold, fontSize = 30.sp
                                , modifier= Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                ,color = Color.Blue
                                )
                        }
                        else{
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(filteredList) { schedule ->
                                    MainScheduleItem(schedule)
                                }
                            }
                        }




                }
            }
        }


}

@Composable
fun TodayScheduleList(schedule: List<ScheduleRequest>) {
    val todayDate = LocalDate.now(ZoneId.of("Asia/Seoul")).toString() // "yyyy-MM-dd" 형식

    val todaySchedules = schedule.filter { it.scheduleDate.toString() == todayDate }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Today's Schedule",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Cyan,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (todaySchedules.isEmpty()) {
            Text(
                text = "오늘 일정이 없습니다.",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        } else {
            LazyColumn {
                items(todaySchedules) { schedule ->
                    MainScheduleItem(schedule)
                }
            }
        }
    }
}
    @Composable
    fun MainScheduleItem(schedule: ScheduleRequest) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFB2DFDB)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth())
                {
                    Text(text =  schedule.title, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Category: ${schedule.category}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "Date: ${schedule.scheduleDate}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }




@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    MainView("tlgud119@naver.com",context = LocalContext.current, navController = rememberNavController()
    , viewModel = ScheduleViewModel(ScheduleRepository(RetrofitClient.getInstance(context = LocalContext.current))))
}