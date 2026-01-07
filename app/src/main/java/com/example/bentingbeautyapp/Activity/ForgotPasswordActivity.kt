package com.example.bentingbeautyapp.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bentingbeautyapp.R
import com.example.bentingbeautyapp.ViewModel.AuthViewModel
import com.example.bentingbeautyapp.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    private val viewModel = AuthViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var email = binding.FPEmailForm.text.toString()

        binding.apply {
            FPAlertForm.visibility = View.GONE

            forgotPassBtn.setOnClickListener {
                email = FPEmailForm.text.toString()
                if (email.isEmpty()) {
                    FPAlertForm.text = "Isi Formulir!"
                    FPAlertForm.visibility = View.VISIBLE
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    FPAlertForm.text = "Format email tidak valid!"
                    FPAlertForm.visibility = View.VISIBLE
                } else {
                    FPAlertForm.visibility = View.GONE
                    viewModel.forgotPass(email)
                }
            }
        }

        viewModel.forgotPassResult.observe(this) {
            result ->

            if (result) {
                Toast.makeText(this, "Email ditemukan", Toast.LENGTH_SHORT).show()
            }else {
                binding.FPAlertForm.text = "Email tidak terdaftar"
                binding.FPAlertForm.visibility = View.VISIBLE
            }
        }
    }
}