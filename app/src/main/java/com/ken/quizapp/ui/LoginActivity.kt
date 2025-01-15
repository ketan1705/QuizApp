package com.ken.quizapp.ui

import android.R.attr.text
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ken.quizapp.MainActivity
import com.ken.quizapp.R
import com.ken.quizapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        supportActionBar?.hide()

        binding.button.setOnClickListener{
            Firebase.auth.createUserWithEmailAndPassword(
                binding.tlEmail.editText?.text.toString(),
                binding.tlPassword.editText?.text.toString()
            ).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(this,"User Created !!!",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Something Went Wrong !!!",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}