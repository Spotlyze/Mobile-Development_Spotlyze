package com.bangkit.spotlyze.ui.camera

import androidx.lifecycle.ViewModel
import com.bangkit.spotlyze.data.repository.SkinRepository

class CameraViewModel(private val repository: SkinRepository) : ViewModel() {

//    private var _result = MutableLiveData<Result<ClassifySkinResponse>>()
//    val result: LiveData<Result<ClassifySkinResponse>> = _result
//
//
//    fun classifySkin(recommendation: String, imageUri: Uri, context: Context) {
//        _result.value = Result.Loading
//        viewModelScope.launch {
//            _result.value = repository.classifySkin(recommendation, imageUri, context)
//        }
//    }
}