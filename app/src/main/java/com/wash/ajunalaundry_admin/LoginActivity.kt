package com.wash.ajunalaundry_admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wash.arjunalaundry_admin.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}