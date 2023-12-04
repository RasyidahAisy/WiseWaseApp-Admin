package com.wash.ajunalaundry_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wash.arjunalaundry_admin.R
import com.wash.arjunalaundry_admin.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePasswordBinding
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.checkBox4.setOnCheckedChangeListener { _, isChecked -> // If the checkbox is checked, show the password.
            // Otherwise, hide the password.
            binding.editTextTextPassword2.transformationMethod = if (isChecked) null else PasswordTransformationMethod.getInstance()
            binding.editTextPasswordSekarang.transformationMethod = if (isChecked) null else PasswordTransformationMethod.getInstance()
            binding.editTextTextPassword2.clearFocus()
        }

        binding.button5.setOnClickListener {

            if (binding.editTextTextPassword2.length() < 6){
                Toast.makeText(this, "Password Harus Lebih Dari 6 Karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.editTextPasswordSekarang.text.isEmpty() || binding.editTextEmail.text.isEmpty()){
                Toast.makeText(this, "Anda Harus Memasukkan Email Dan Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(binding.editTextEmail.text.toString(),
                binding.editTextPasswordSekarang.text.toString()
            ).addOnSuccessListener {


                val user = Firebase.auth.currentUser
                val newPassword = binding.editTextTextPassword2.text.toString()


                user!!.updatePassword(newPassword)
                    .addOnSuccessListener {
                        Log.d("ChangePass", "Berhasil Mengganti Password")
                        Toast.makeText(this, "Anda Berhasil Mengubah Password Anda", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Log.e("ChangePass", "onCreateView: Gagal",it)
                    }

             finish()

            }.addOnFailureListener{
                Toast.makeText(this, "Password Lama Anda Salah", Toast.LENGTH_SHORT).show()

            }
            Toast.makeText(this, "Password Anda Salah", Toast.LENGTH_SHORT).show()
        }
    }
}