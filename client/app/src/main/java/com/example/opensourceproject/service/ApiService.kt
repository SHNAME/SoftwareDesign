package com.example.opensourceproject.service

import com.example.opensourceproject.data.dto.cafeteria.CafeteriaResponse
import com.example.opensourceproject.data.dto.login.LoginRequest
import com.example.opensourceproject.data.dto.schedule.ScheduleRequest
import com.example.opensourceproject.data.dto.login.UserDto
import com.example.opensourceproject.data.dto.schedule.AddScheduleRequest
import com.example.opensourceproject.data.dto.schedule.ModificationRequest
import com.example.opensourceproject.data.dto.timetable.AddTimetableDto
import com.example.opensourceproject.data.dto.timetable.GetTimetableDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface   ApiService {
    @POST("/login")
  fun login(@Body user: LoginRequest): Call<Void>

  @POST("/login/join")
  fun signUp(@Body newUser: UserDto): Call<Void>


  //일정조회
  @GET("/schedule")
  suspend fun getMonthList(
    @Query("year") year: Int,
    @Query("month") month: Int
  ): Response<List<ScheduleRequest>>

  //일정 추가
  @POST("/schedule")
  fun addSchedule(
    @Body request: AddScheduleRequest
  ): Call<String>




  @PUT("/schedule/{id}")
  suspend fun modifySchedule(
    @Path("id") id: Long,
    @Body request: ModificationRequest
  ): Response<String>

  //일정 삭제
  @DELETE("/schedule/{id}")
  fun deleteSchedule(
    @Path("id") id: Long
  ): Call<Boolean>


  //이메일을 기반으로 오늘을 포함한 사용자의 모든 데이터를 가져온다.
  @GET("/schedule/category")
  suspend fun getListByUserEmail():
          Response<List<ScheduleRequest>>


  // 시간표 조회 (사용자 이메일 기반)
  @GET("/timetable")
  suspend fun getTimeTable(): Response<List<GetTimetableDto>>

  // 시간표 추가
  @POST("/timetable")
  suspend fun addTimeTable(
    @Body request: AddTimetableDto
  ): Response<Void>

  // 시간표 수정
  @PUT("/timetable/{id}")
  suspend fun modifyTimeTable(
    @Path("id") id: Long,
    @Body request: AddTimetableDto
  ): Response<Void>

  //  특정 시간표 삭제
  @DELETE("/timetable/{id}")
  suspend fun deleteTimeTable(
    @Path("id") id: Long
  ): Response<Boolean>

  //  전체 시간표 삭제
  @DELETE("/timetable")
  suspend fun deleteAllTimeTable(): Response<Boolean>


  //자연계 및 학생회관 식단 가져오기
  @GET("/cafeteria")
  @Headers("Accept: application/json")
  suspend fun getCafeteria():Response<CafeteriaResponse>




}