package com.bangkit.spotlyze.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.ui.camera.CameraFragment
import com.prayatna.spotlyze.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        goAnalyze()
    }

    private fun goAnalyze() {
        binding.buttonSubmit.setOnClickListener {
            if (isQuizComplete()) {
                setupAnalyze()
            } else {
                Message.toast(this, "Please complete the quiz first")
            }
        }
    }

    private fun setupAnalyze() {
        val pictureUri = intent.getStringExtra(CameraFragment.EXTRA_RESULT)
        Log.d("okhttp", "pictureUri: $pictureUri")
        val intent = Intent(this, AnalyzeActivity::class.java)
        intent.putExtra(SKIN_TYPE, getSkinType())
        intent.putExtra(SKIN_SENSITIVITY, getSkinSensitivity())
        intent.putStringArrayListExtra(CONCERN, getConcerns())
        intent.putExtra(IMAGE_URI, pictureUri)
        startActivity(intent)
        Log.d("okhttp", "goAnalyze: ${getSkinType()}\n ${getSkinSensitivity()}\n ${getConcerns()}")
        finish()
    }

    private fun getSkinType(): String? {
        return when (binding.questionSkinType.checkedRadioButtonId) {
            binding.radioDry.id -> "Dry"
            binding.radioOily.id -> "Oily"
            binding.radioCombination.id -> "Combination"
            else -> null
        }
    }

    private fun getSkinSensitivity(): String? {
        return when (binding.questionSkinSensitivity.checkedRadioButtonId) {
            binding.radioNormal.id -> "Normal"
            binding.radioSensitive.id -> "Sensitive"
            else -> null
        }
    }

    private fun getConcerns(): ArrayList<String?> {
        val concerns = arrayListOf<String?>()
        if (binding.checkboxAcne.isChecked) concerns.add("Acne and Breakouts")
        if (binding.checkboxBrightening.isChecked) concerns.add("Skin Brightening")
        if (binding.checkboxDarkCircles.isChecked) concerns.add("Dark Circles and Eye Bags")
        if (binding.checkboxDullness.isChecked) concerns.add("Skin Dullness")
        if (binding.checkboxHydration.isChecked) concerns.add("Hydration and Nourishment")
        if (binding.checkboxTexture.isChecked) concerns.add("Uneven Skin Texture")
        if (binding.checkboxWrinkles.isChecked) concerns.add("Fine Lines and Wrinkles")
        return concerns
    }

    private fun isQuizComplete(): Boolean {
        return getSkinType() != null && getSkinSensitivity() != null && getConcerns().isNotEmpty()
    }

    companion object {
        const val SKIN_TYPE = "skin_type"
        const val SKIN_SENSITIVITY = "skin_sensitivity"
        const val CONCERN = "concerns"
        const val IMAGE_URI = "image_uri"
    }
}