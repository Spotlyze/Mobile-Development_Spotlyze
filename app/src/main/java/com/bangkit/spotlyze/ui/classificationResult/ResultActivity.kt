package com.bangkit.spotlyze.ui.classificationResult

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.spotlyze.data.source.RecommendationItem
import com.bangkit.spotlyze.ui.adapter.RecommendationAdapter
import com.bangkit.spotlyze.ui.main.MainActivity
import com.bangkit.spotlyze.ui.quiz.AnalyzeActivity.Companion.EXTRA_RECOMMEND
import com.prayatna.spotlyze.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION") val recommend =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(
                    EXTRA_RECOMMEND,
                    RecommendationItem::class.java
                )
            } else {
                intent.getParcelableArrayListExtra(EXTRA_RECOMMEND)
            }
        if (recommend != null) {
            setupAdapter(recommend)
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        })
    }

    private fun setupAdapter(items: List<RecommendationItem>) {
        val adapter = RecommendationAdapter(items)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            this.adapter = adapter
        }
    }
}
