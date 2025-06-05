package com.example.opensourceproject.view.bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.opensourceproject.R

@Composable
fun BottomBar(navController: NavController, email:String?) {
    var selectedItem by remember { mutableStateOf(0) }


    androidx.compose.material3.BottomAppBar(
        modifier = Modifier.fillMaxWidth()
            .height(80.dp).padding(bottom = 3.dp),
        containerColor = Color.White, // 바의 배경 색상
        contentColor = Color.Black,        // 아이콘과 텍스트 색상
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            IconButton(onClick = {
                selectedItem = 1
                navController.navigate("mainView/$email")
            }, modifier = Modifier.padding(bottom = 0.dp,)) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_home_24),
                    contentDescription = "홈",
                    tint = if (selectedItem == 1) Color.Red else Color.Black // 선택된 아이템에 따라 색상 변경

                )
            }
        }
        Spacer(modifier = Modifier.weight(1f)) // 가운데 여백 추가
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        )
        {
            IconButton(onClick = { selectedItem = 2
                navController.navigate("ScheduleView")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = "일정",
                    tint = if (selectedItem == 2) Color.Yellow else Color.Black// 선택된 아이템에 따라 색상 변경
                )
            }

        }

        Spacer(modifier = Modifier.weight(1f)) // 가운데 여백 추가
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        )
        {
            IconButton(onClick = { selectedItem = 3
            navController.navigate("timetable")}) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_cal_24),
                    contentDescription = "시간표",
                    tint = if (selectedItem == 3) Color.Yellow else Color.Black // 선택된 아이템에 따라 색상 변경
                )
            }
        }



        Spacer(modifier = Modifier.weight(1f)) // 가운데 여백 추가
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        )
        {
            IconButton(onClick = {
               navController.navigate("cafeteria")
                selectedItem = 4
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_fastfood_24),
                    contentDescription = "식단",
                    tint = if (selectedItem == 5) Color.Yellow else Color.Black// 선택된 아이템에 따라 색상 변경
                )
            }
        }



    }
}


@Composable
@Preview(showBackground = true)
fun BottomAppBarPreview()
{
    BottomBar(navController = rememberNavController(),"tlgud119@naver.com")
}