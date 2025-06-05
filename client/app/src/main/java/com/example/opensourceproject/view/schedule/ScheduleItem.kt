package com.example.opensourceproject.view.schedule

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.opensourceproject.R
import com.example.opensourceproject.data.dto.schedule.ScheduleRequest
import com.example.opensourceproject.data.scheduleModel.ScheduleRepository
import com.example.opensourceproject.data.scheduleModel.ScheduleViewModel
import com.example.opensourceproject.service.RetrofitClient
import java.time.LocalDate

@Composable
fun ScheduleItem(schedule: ScheduleRequest, viewModel: ScheduleViewModel,
                 navController: NavController,context: Context) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            ,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.ScheduleItem)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = schedule.title,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 40.sp,
                    color = Color.Black
                )

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_more_vert_24),
                            contentDescription = "옵션"
                        )
                    }

                    // DropdownMenu 추가
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("수정",fontSize = 20.sp, fontWeight = FontWeight.Bold
                            ,color = Color.Black)},
                            onClick = {
                                expanded = false
                                viewModel.modificationRequest.value=schedule
                                //수정을 누르면 수정 화면으로 이동한다.
                                navController.navigate("modification")

                            }
                            ,colors= MenuDefaults.itemColors(
                                contentColorFor(backgroundColor = Color.White)
                            )

                        )
                        DropdownMenuItem(
                            text = { Text("삭제",fontSize = 20.sp, fontWeight = FontWeight.Bold
                                ,color = Color.Black)},
                            onClick = {
                                expanded = false
                               viewModel.deleteSchedule(schedule.id)
                                if(!viewModel.toastMessage.value.isNullOrBlank()){
                                 Toast.makeText(context, viewModel.toastMessage.value, Toast.LENGTH_LONG).show()
                                    viewModel.toastMessage.value = null
                                }
                                else{

                                    navController.popBackStack()
                                    navController.navigate("ScheduleView")

                                }
                            },
                            colors= MenuDefaults.itemColors(
                                contentColorFor(backgroundColor = Color.White)
                            )
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_24),
                    contentDescription = "카테고리", modifier = Modifier.padding(end = 5.dp)
                 ) // 선택된 아이템에 따라 색상 변경
                Text(text = "Category:  ${schedule.category}",fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.padding(bottom = 20.dp))
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                    contentDescription = "날짜", modifier = Modifier.padding(end = 5.dp)
                )
                Text(text = "Date: ${schedule.scheduleDate}",fontSize = 18.sp)
            }

        }
    }
}
@Preview(showBackground = true)
@Composable
fun ScheduleItemPreview() {
    val item :ScheduleRequest = ScheduleRequest(1,"제목","카테고리",
        LocalDate.now())
    ScheduleItem(schedule = item,
        viewModel = ScheduleViewModel(
            ScheduleRepository(RetrofitClient.getInstance
            (context = LocalContext.current))
        ),   navController = rememberNavController(),context = LocalContext.current)

}