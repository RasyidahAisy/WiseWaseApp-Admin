package com.wash.ajunalaundry_admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wash.arjunalaundry_admin.databinding.ActivityLoginBinding
import com.wash.arjunalaundry_admin.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}