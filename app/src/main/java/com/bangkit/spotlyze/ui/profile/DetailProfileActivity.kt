package com.bangkit.spotlyze.ui.profile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.prayatna.spotlyze.R
import com.prayatna.spotlyze.databinding.ActivityDetailProfileBinding

class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding

    private val viewModel: ProfileViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_profile)
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

}