package com.wash.ajunalaundry_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.wash.arjunalaundry_admin.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChangePassword.setOnClickListener {
            FirebaseAuth.getInstance().sendPasswordResetEmail(binding.txtEmail.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil Mengirim Email Pemulihan", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal Mengirim Email Pemulihan", Toast.LENGTH_SHORT).show()
                    Log.e("ForgetPassword", "onCreate: Gagal", it)
                }
        }

    }
}