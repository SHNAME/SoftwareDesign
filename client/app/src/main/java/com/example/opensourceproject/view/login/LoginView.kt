package com.example.opensourceproject.view.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.opensourceproject.R
import com.example.opensourceproject.data.dto.login.LoginRequest
import com.example.opensourceproject.service.RetrofitClient
import retrofit2.Call
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(context: Context, navController: NavController)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            Text(text = "Jup",fontSize = 30.sp, color = Color.White, fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold  )

            Icon(
                painter = painterResource(id = R.drawable.baseline_done_24),
                contentDescription = "체크",
                tint = Color.White, modifier = Modifier
                    .size(30.dp)
                    .padding(start = 5.dp)
            )
        }


        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        OutlinedTextField(value = email,
            onValueChange = { email = it }, label = { Text(text = "이메일") },
            modifier = Modifier.padding(4.dp), keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ) ,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Green,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Black
                , focusedTextColor = Color.Black ,
                unfocusedContainerColor = Color.White
                , focusedContainerColor = colorResource(id = R.color.Yellow)
            ),
           maxLines = 1,
            minLines = 1
        )

        OutlinedTextField(value = password, onValueChange = {password = it},
            label = { Text(text = "비밀번호") },
            modifier =Modifier.padding(4.dp), keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Green,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Black
                , focusedTextColor = Color.Black ,
                unfocusedContainerColor = Color.White
                , focusedContainerColor = colorResource(id = R.color.Yellow)
            )
            , maxLines = 1,
            minLines = 1


        )

        Spacer(modifier = Modifier.height(16.dp))

        // 로그인 버튼
        Button(
            onClick = {
                when {
                    email.isEmpty() -> {
                        Toast.makeText(context, "Email is required", Toast.LENGTH_SHORT).show()
                    }
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                    }
                    password.isEmpty() -> {
                        Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val user = LoginRequest(email, password)


                        RetrofitClient.instance.login(user).enqueue(object : retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    val jwtToken = response.headers()["Authorization"]


                                    if (jwtToken != null) {
                                        saveToken(context, jwtToken) // JWT 토큰 저장
                                        navController.navigate("mainView/${email}") // 메인 화면으로 이동
                                    } else {
                                        Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "로그인 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(context, "네트워크 오류 발생!", Toast.LENGTH_SHORT).show()
                            }


                        })
                    }
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .size(width = 150.dp, height = 50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            )
        ) {
            Text("로그인", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = {
            navController.navigate("signup") }) {
            Text(text = "회원가입", color = Color.Yellow, fontWeight = FontWeight.Bold)
        }
    }
}
///로그아웃 기능
fun logout(context: Context, navController: NavController) {
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().remove("jwt_token").apply() // 저장된 JWT 삭제

    Toast.makeText(context, "로그아웃 완료", Toast.LENGTH_SHORT).show()

    // 로그인 화면으로 이동하면서 백스택 제거
    navController.navigate("login") {
        popUpTo("mainView") { inclusive = true } // 뒤로 가기 방지
    }
}
//토큰을 저장
fun saveToken(context: Context, token: String) {
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("jwt_token", token).apply()
}

@Composable
@Preview(showBackground = true)
fun LoginViewPreview() {
    val context = LocalContext.current
    val navController = rememberNavController() //
    val email = remember { mutableStateOf("") } //

    LoginView(context = context, navController = navController)
}
