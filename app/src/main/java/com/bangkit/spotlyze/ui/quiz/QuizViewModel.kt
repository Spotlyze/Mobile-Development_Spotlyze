package com.bangkit.spotlyze.ui.quiz

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.spotlyze.data.remote.response.ClassifySkinResponse
import com.bangkit.spotlyze.data.repository.SkinRepository
import com.bangkit.spotlyze.data.source.Result
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: SkinRepository) : ViewModel() {

    private var _analyzeState = MutableLiveData<Result<ClassifySkinResponse>>()
    val analyzeState: LiveData<Result<ClassifySkinResponse>> = _analyzeState

    fun classifySkin(
        skinType: String,
        skinSensitivity: String,
        concerns: List<String>,
        imageUri: Uri,
        context: Context
    ) {
        _analyzeState.value = Result.Loading
        viewModelScope.launch {
         _analyzeState.value = repository.classifySkin(skinType, skinSensitivity, concerns, imageUri, context)
        }
    }

}