package com.bangkit.spotlyze.ui.quiz

import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.ui.SkinViewModelFactory
import com.bangkit.spotlyze.ui.history.DetailHistoryActivity
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
        setupAnimation()
    }

    private fun setupAnimation() {
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.2f)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.2f)
        val animator = android.animation.ObjectAnimator.ofPropertyValuesHolder(binding.imageAnimation, scaleX, scaleY).apply {
            duration = 1000
            repeatCount = android.animation.ValueAnimator.INFINITE
            repeatMode = android.animation.ValueAnimator.REVERSE
        }
        animator.start()
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
                Result.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                    binding.btnAnalyze.visibility = View.GONE
                }
                is Result.Success -> {
                    Log.d("okhttp", "success: ${data.data}")
                    val intent = Intent(this, DetailHistoryActivity::class.java)
                    intent.putExtra(CLASSIFY_RESULT, data.data.historyId.toString())
                    Log.d("okhttp", "history id: ${data.data.historyId}")
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    companion object {
        const val CLASSIFY_RESULT = "classify_result"
    }
}