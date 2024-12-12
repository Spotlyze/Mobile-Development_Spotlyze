package com.bangkit.spotlyze.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.spotlyze.ui.camera.CameraFragment
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private var skinType: String? = null
    private var skinSensitivity: String? = null
    private val skinConcerns = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

    }

    private fun setupAction() {
        goAnalyze()
    }

    private fun goAnalyze() {
        binding.submitButton.setOnClickListener {
            if (!isQuizComplete()) {
                Toast.makeText(this, "Please complete the quiz", Toast.LENGTH_SHORT).show()
            } else {
                showResults()
                setupAnalyze()
            }
        }
    }

    private fun setupAnalyze() {
        val pictureUri = intent.getStringExtra(CameraFragment.EXTRA_RESULT)
        Log.d("okhttp", "pictureUri: $pictureUri")
        val intent = Intent(this, AnalyzeActivity::class.java)
        intent.putExtra(SKIN_TYPE, skinType)
        intent.putExtra(SKIN_SENSITIVITY, skinSensitivity)
        intent.putStringArrayListExtra(CONCERN, skinConcerns as ArrayList<String>?)
        intent.putExtra(IMAGE_URI, pictureUri)
        startActivity(intent)
        finish()
    }

    private fun isQuizComplete(): Boolean {
        return skinType != null && skinSensitivity != null && skinConcerns.isNotEmpty()
    }

    private fun setupView() {
        val viewPager = binding.viewPager
        val submitButton = binding.submitButton

        val layouts = listOf(
            R.layout.question_1,
            R.layout.question_2,
            R.layout.question_3
        )

        val adapter = QuizPagerAdapter(layouts) { position, view ->
            when (position) {
                0 -> handleSkinTypeInput(view)
                1 -> handleSkinSensitivityInput(view)
                2 -> handleSkinConcernsInput(view)
            }
        }

        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                submitButton.visibility = if (position == layouts.lastIndex) View.VISIBLE else View.GONE
            }
        })
    }

    private fun handleSkinTypeInput(view: View) {
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupSkinType)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            skinType = when (checkedId) {
                R.id.radioDry -> "Dry"
                R.id.radioOily -> "Oily"
                R.id.radioCombination -> "Combination"
                else -> null
            }
        }
    }

    private fun handleSkinSensitivityInput(view: View) {
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupSkinSensitivity)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            skinSensitivity = when (checkedId) {
                R.id.radioNormal -> "Normal"
                R.id.radioSensitive -> "Sensitive"
                else -> null
            }
        }
    }

    private fun handleSkinConcernsInput(view: View) {
        val checkboxes = listOf(
            view.findViewById<CheckBox>(R.id.checkSkinBrightening),
            view.findViewById<CheckBox>(R.id.checkUnevenTexture),
            view.findViewById<CheckBox>(R.id.checkSkinDullness),
            view.findViewById<CheckBox>(R.id.checkHydration),
            view.findViewById<CheckBox>(R.id.checkAcne),
            view.findViewById<CheckBox>(R.id.checkFineLines),
            view.findViewById<CheckBox>(R.id.checkDarkCircles)
        )

        checkboxes.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    skinConcerns.add(checkbox.text.toString())
                } else {
                    skinConcerns.remove(checkbox.text.toString())
                }
            }
        }
    }

    private fun showResults() {
        val results = """
            Skin Type: $skinType
            Skin Sensitivity: $skinSensitivity
            Skin Concerns: ${skinConcerns.joinToString(", ")}
        """.trimIndent()

        Toast.makeText(this, results, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val SKIN_TYPE = "skin_type"
        const val SKIN_SENSITIVITY = "skin_sensitivity"
        const val CONCERN = "concerns"
        const val IMAGE_URI = "image_uri"
    }
}
