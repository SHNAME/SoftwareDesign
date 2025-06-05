package com.example.opensourceproject.service

import android.content.Context
import okhttp3.Interceptor

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "$token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
