package com.example.opensourceproject.data.cafeteriaModel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CafeteriaViewModel(private val repository: CafeteriaRepository) : ViewModel()  {
    // 이미지 데이터를 저장할 StateFlow
    private val _cafeteriaImages = MutableStateFlow<Map<String, Bitmap>?>(null)
    val cafeteriaImages: StateFlow<Map<String, Bitmap>?> = _cafeteriaImages
    var toastMessage = mutableStateOf<String?>(null)


    // 이미지를 불러오는 함수
    fun loadCafeteriaImages() {
        viewModelScope.launch {
            val images = repository.getCafeteriaImages()
            if(images !=null)
            {
                _cafeteriaImages.value = images
            }
            else{
                Log.e("loadCafeteriaImages", "Failed to load images")
                toastMessage.value = "식단 이미지 가져오기 실패"
            }
        }
    }



}