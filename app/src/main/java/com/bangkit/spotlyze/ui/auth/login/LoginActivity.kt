package com.bangkit.spotlyze.ui.auth.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.local.model.UserModel
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.prayatna.spotlyze.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailInput.emailEditText.toString()
            val password = binding.passwordInput.passwordEditText.text.toString()
            val user = UserModel(email, password)
            viewModel.saveSession(user)
        }
    }
}