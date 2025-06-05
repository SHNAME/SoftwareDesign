package com.example.opensourceproject.data.cafeteriaModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.example.opensourceproject.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream

class CafeteriaRepository(private val api: ApiService) {

    suspend fun getCafeteriaImages(): MutableMap<String,Bitmap>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCafeteria()
                if (response.isSuccessful) {
                    Log.d("CafeteriaAPI", "Get Photo Success")
                    val map: MutableMap<String, Bitmap> = mutableMapOf()
                    map["natural"] = decodeBase64(response.body()!!.natural)
                    map["student"] = decodeBase64(response.body()!!.student)
                    map

                } else {
                    Log.e("CafeteriaAPI", "Get Photo fail: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("CafeteriaAPI", "NetWork Error: ${e.message}")
                null
            }
        }
    }



    private fun decodeBase64(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String.split(",")[1], Base64.DEFAULT)
        return BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
    }

}