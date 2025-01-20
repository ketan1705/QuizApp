package com.ken.quizapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ken.quizapp.R
import com.ken.quizapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        supportActionBar?.hide()

        binding.btnSignUp.setOnClickListener {
            Firebase.auth.createUserWithEmailAndPassword(
                binding.tlEmail.editText?.text.toString(),
                binding.tlPassword.editText?.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, DashBoardActivity::class.java))
                    finish()
                    Toast.makeText(this, "User Created !!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Something Went Wrong !!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            Firebase.auth.signInWithEmailAndPassword(
                binding.tlEmail.editText?.text.toString(),
                binding.tlPassword.editText?.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, DashBoardActivity::class.java))
                    finish()
                    Toast.makeText(this, "User Logged In !!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Something Went Wrong !!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.alreadyHaveAccountTxt.setOnClickListener {
            binding.tlName.visibility = View.GONE
            binding.btnSignUp.visibility = View.GONE
            binding.alreadyHaveAccountTxt.visibility = View.GONE
            binding.btnSignIn.visibility = View.VISIBLE
            binding.createAccountTxt.visibility = View.VISIBLE
        }
        binding.createAccountTxt.setOnClickListener {
            binding.tlName.visibility = View.VISIBLE
            binding.btnSignUp.visibility = View.VISIBLE
            binding.alreadyHaveAccountTxt.visibility = View.VISIBLE
            binding.btnSignIn.visibility = View.GONE
            binding.createAccountTxt.visibility = View.GONE
        }

    }
}