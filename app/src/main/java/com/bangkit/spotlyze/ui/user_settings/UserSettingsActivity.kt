package com.bangkit.spotlyze.ui.user_settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.prayatna.spotlyze.databinding.ActivityUserSetitngsBinding

class UserSettingsActivity : AppCompatActivity() {

    private var _binding: ActivityUserSetitngsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserSetitngsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setSupportActionBar(binding.profileToolBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }
}