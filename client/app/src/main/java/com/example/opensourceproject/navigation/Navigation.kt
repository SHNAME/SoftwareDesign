package com.example.opensourceproject.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.opensourceproject.data.cafeteriaModel.CafeteriaRepository
import com.example.opensourceproject.data.cafeteriaModel.CafeteriaViewModel
import com.example.opensourceproject.data.scheduleModel.ScheduleRepository
import com.example.opensourceproject.data.scheduleModel.ScheduleViewModel
import com.example.opensourceproject.data.timeTableModel.TimetableRepository
import com.example.opensourceproject.data.timeTableModel.TimetableViewModel
import com.example.opensourceproject.service.RetrofitClient
import com.example.opensourceproject.view.cafeteria.CafeteriaScreen
import com.example.opensourceproject.view.cafeteria.ZoomableImageScreen
import com.example.opensourceproject.view.login.LoginView
import com.example.opensourceproject.view.login.SignupView
import com.example.opensourceproject.view.main.MainView
import com.example.opensourceproject.view.schedule.AddScheduleScreen
import com.example.opensourceproject.view.schedule.ModificationScreen
import com.example.opensourceproject.view.schedule.ScheduleScreen
import com.example.opensourceproject.view.timeTable.TimeTableScreen

@Composable
fun Navigation(context: Context, navController: NavHostController) {
    //jwt 토큰을 포함하는 viewModel 정의
    val repository = ScheduleRepository(RetrofitClient.getInstance(context))
    val scheduleViewModel = ScheduleViewModel(repository)

    val timeTableViewModel = TimetableViewModel(TimetableRepository(RetrofitClient.getInstance(context)))
    val cafeteriaViewModel = CafeteriaViewModel(CafeteriaRepository(RetrofitClient.getInstance(context)))

    NavHost(navController = navController, startDestination = "login")
    {
        //로그인 화면
        composable("login") {
            LoginView(context, navController)
        }

        //회원 가입 화면
        composable("signup") {
            SignupView(context, navController, )
        }

        //메인화면
        composable("mainView/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            MainView(email, navController,context,scheduleViewModel)
              }
        // 일정 메인 화면
        composable("ScheduleView") { backStackEntry ->
            ScheduleScreen(
                viewModel =scheduleViewModel , navController = navController,
                )
        }
        
        //일정 추가,수정 화면
        composable("AddSchedule")
        { backStackEntry ->
            AddScheduleScreen(navController = navController,viewModel = scheduleViewModel)
        }

        //일정 수정화면
        composable("modification")
        { backStackEntry ->
            ModificationScreen(
                navController = navController,
                viewModel = scheduleViewModel)
        }

        //시간표 화면
        composable("timetable")
        { backStackEntry ->
            TimeTableScreen(viewModel = timeTableViewModel,navController=navController)
        }

        //식단 화면
        composable("cafeteria")
        { backStackEntry ->
            CafeteriaScreen(viewModel =cafeteriaViewModel ,navController=navController,context)
        }
        composable("zoom/{type}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            val image = navController.previousBackStackEntry?.savedStateHandle?.get<String>("image")

            if (image != null) {
                ZoomableImageScreen(navController, image)
            }
        }



    }

}