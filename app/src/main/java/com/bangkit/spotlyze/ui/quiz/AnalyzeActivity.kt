package com.bangkit.spotlyze.ui.quiz

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.SkinViewModelFactory
import com.prayatna.spotlyze.databinding.ActivityAnalyzeBinding

class AnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyzeBinding
    private val viewModel: QuizViewModel by viewModels {
        SkinViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.btnAnalyze.setOnClickListener {
            classifySkin()
        }
    }

    private fun classifySkin() {
        val skinType = intent.getStringExtra(QuizActivity.SKIN_TYPE)
        val skinSensitivity = intent.getStringExtra(QuizActivity.SKIN_SENSITIVITY)
        val concerns = intent.getStringArrayListExtra(QuizActivity.CONCERN)
        val imageUri = intent.getStringExtra(QuizActivity.IMAGE_URI)
        if (skinType != null && skinSensitivity != null && concerns != null && imageUri != null) {
            viewModel.classifySkin(skinType, skinSensitivity, concerns, imageUri.toUri(), this)
        }
    }

    private fun setupViewModel() {
        viewModel.analyzeState.observe(this) { data ->
            when (data) {
                is Result.Error -> {
                    Log.e("okhttp", "error: ${data.error}")
                }
                Result.Loading -> {}
                is Result.Success -> {
                    Log.d("okhttp", "success: ${data.data}")
                    finish()
                }
            }
        }
    }
}