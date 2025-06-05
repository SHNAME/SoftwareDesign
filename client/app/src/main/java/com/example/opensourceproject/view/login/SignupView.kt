package com.example.opensourceproject.view.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.opensourceproject.R
import com.example.opensourceproject.data.dto.login.UserDto
import com.example.opensourceproject.service.RetrofitClient
import retrofit2.Call
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupView(context: Context, navController: NavController)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = {
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier.padding(15.dp)
        ) {
            Text(text = "Back", color = Color.White, fontWeight = FontWeight.ExtraBold
            , fontSize = 15.sp)
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(top = 60.dp)
        .background(color = Color.Black), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {

        Text(text = "회원 가입",fontSize = 30.sp, color = Color.White,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        OutlinedTextField(value = email,
            onValueChange = { email = it }, label = { Text(text = "이메일") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Green,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Black
                , focusedTextColor = Color.Black ,
                unfocusedContainerColor = Color.White
                , focusedContainerColor = colorResource(id = R.color.Yellow)
            ),
            minLines = 1
            ,maxLines = 1
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        OutlinedTextField(value = password, onValueChange = {password = it},
            label = { Text(text = "비밀번호") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Green,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Black
                , focusedTextColor = Color.Black ,
                unfocusedContainerColor = Color.White
                , focusedContainerColor = colorResource(id = R.color.Yellow)
            )
            , minLines = 1
            ,maxLines = 1
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        OutlinedTextField(value = nickname, onValueChange = {nickname = it},
            label = { Text(text = "이름") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done)
            ,  colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Green,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Black
                , focusedTextColor = Color.Black ,
                unfocusedContainerColor = Color.White
                , focusedContainerColor = colorResource(id = R.color.Yellow)
            ),
            minLines = 1
            ,maxLines = 1
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))


        Button(
            modifier = Modifier.width(220.dp),
            colors = ButtonColors(containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.Black
                ),
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
                    nickname.isEmpty() -> {
                        Toast.makeText(context, "Nickname is required", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                      val newUser = UserDto(email,password,nickname);
                        RetrofitClient.instance.signUp(newUser).enqueue(object : retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("login") // 로그인 화면으로 이동
                                } else {
                                    Toast.makeText(context, "회원가입 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.e("SignupView", "회원가입 실패: ${t.message}")
                                Toast.makeText(context, "네트워크 오류 발생!", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        ) {
            Text("회원가입")
        }

    }
}

@Composable
@Preview(showBackground = true)
fun SignupViewPreview()
{
    SignupView(context = LocalContext.current, navController = rememberNavController())
}