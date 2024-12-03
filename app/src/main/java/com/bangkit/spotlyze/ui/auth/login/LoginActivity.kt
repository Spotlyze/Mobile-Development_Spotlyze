package com.bangkit.spotlyze.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.spotlyze.data.local.model.UserModel
import com.bangkit.spotlyze.data.source.Result
import com.bangkit.spotlyze.helper.Message
import com.bangkit.spotlyze.ui.UserViewModelFactory
import com.bangkit.spotlyze.ui.main.MainActivity
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
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.loadingStatus.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    Message.toast(this, result.error)
                }

                is Result.Loading -> {
                    Log.d("Loading", "Loading")
                }

                is Result.Success -> {
                    val data = result.data
                    val user = UserModel(
                        data.userId!!,
                        data.userName!!,
                        data.token!!,
                        true
                    )
                    viewModel.saveSession(user)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun setupAction() {
        login()
    }

    private fun login() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailInput.emailEditText.text.toString()
            val password = binding.passwordInput.passwordEditText.text.toString()
            Log.d("okhttp", "email: $email, pass: $password")
            viewModel.login(email, password)
        }
    }
}