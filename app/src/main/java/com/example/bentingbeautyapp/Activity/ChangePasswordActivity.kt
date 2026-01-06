package com.example.bentingbeautyapp.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bentingbeautyapp.R
import com.example.bentingbeautyapp.ViewModel.AuthViewModel
import com.example.bentingbeautyapp.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChangePasswordBinding
    private val viewModel = AuthViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            CPAlertForm.visibility = View.GONE
            changePassBtn.setOnClickListener {
                val password = CPPasswordForm.text.toString().trim()
                val passwordConf = CPPasswordForm2.text.toString().trim()

                if (password.isEmpty() || passwordConf.isEmpty()) {
                    CPAlertForm.text = "Lengkapi Formulir!"
                    CPAlertForm.visibility = View.VISIBLE
                }
            }
        }
    }
}