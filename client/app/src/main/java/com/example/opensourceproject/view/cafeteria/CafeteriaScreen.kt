package com.example.opensourceproject.view.cafeteria

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.opensourceproject.data.cafeteriaModel.CafeteriaRepository
import com.example.opensourceproject.data.cafeteriaModel.CafeteriaViewModel
import com.example.opensourceproject.service.RetrofitClient
import com.example.opensourceproject.view.bar.BottomBar
import java.io.ByteArrayOutputStream

@Composable
fun CafeteriaScreen(viewModel: CafeteriaViewModel, navController: NavController,context: Context) {
    val cafeteriaImages by viewModel.cafeteriaImages.collectAsState()

    LaunchedEffect(1) {
        viewModel.loadCafeteriaImages()
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
        Column(modifier = Modifier.weight(1f).padding(10.dp)) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("식단표", fontSize = 30.sp, color = Color.Green)
            Spacer(modifier = Modifier.height(40.dp))

            // 자연계 식단표
            Text("자연계", fontSize = 20.sp, color = Color.Yellow, modifier = Modifier.padding(bottom = 5.dp))
            cafeteriaImages?.get("natural")?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "자연계 식당 식단표",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clickable {
                            navController.currentBackStackEntry?.savedStateHandle?.set("image", bitmapToBase64(bitmap))
                            navController.navigate("zoom/natural")
                        }
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 학생회관 식단표
            Text("학생회관", fontSize = 20.sp, color = Color.Yellow, modifier = Modifier.padding(bottom = 5.dp))
            cafeteriaImages?.get("student")?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "학생회관 식당 식단표",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clickable {
                            navController.currentBackStackEntry?.savedStateHandle?.set("image", bitmapToBase64(bitmap))
                            navController.navigate("zoom/student")
                        }
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }
        }

        BottomBar(navController = navController, email = "imsi")
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}



@Preview(showBackground = true)
@Composable
fun CafeteriaPreView() {

    CafeteriaScreen(CafeteriaViewModel(CafeteriaRepository(RetrofitClient.getInstance(context = LocalContext.current))),
        navController = rememberNavController(),context = LocalContext.current)

}