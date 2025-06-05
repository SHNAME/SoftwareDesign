package com.example.opensourceproject.view.cafeteria


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.util.Base64
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.opensourceproject.R
import kotlin.math.max
import kotlin.math.min

@Composable
fun ZoomableImageScreen(navController: NavController, base64: String) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = max(1f, min(scale * zoom, 5f))
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }, modifier = Modifier.padding(top=15.dp,start = 10.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_clear_24),
                contentDescription = "뒤로 가기",
                tint = Color.Green
            )

        }
        Image(
            bitmap = convertBitmapToImageBitmap(base64ToBitmap(base64)) ,
            contentDescription = "Zoomed Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
        )


        // 뒤로 가기 기능 추가
        BackHandler {
            navController.popBackStack()
        }
    }
}



fun base64ToBitmap(base64String: String): Bitmap {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}
fun convertBitmapToImageBitmap(bitmap: Bitmap): ImageBitmap {
    return bitmap.asImageBitmap()
}