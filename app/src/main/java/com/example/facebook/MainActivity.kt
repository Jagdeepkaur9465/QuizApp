package com.example.facebook

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facebook.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Facebook SDK
        callbackManager = CallbackManager.Factory.create()

        binding.signUpButton.setOnClickListener {
            signUpUser()
        }

        binding.logInButton.setOnClickListener {
            logInUser()
            startActivity(Intent(this, MainActivity2::class.java))
        }
        binding.goaheadBtn.setOnClickListener {
            // Start MainActivity2
            startActivity(Intent(this, MainActivity2::class.java))
        }


        // Register callback for Facebook login
        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // Handle successful login
                Toast.makeText(applicationContext, "Facebook Login Successful", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                // Handle canceled login
                Toast.makeText(applicationContext, "Facebook Login Canceled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                // Handle error in login
                Toast.makeText(applicationContext, "Facebook Login Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun signUpUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Sign Up Failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Email and password must not be empty",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun logInUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Login Failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Email and password must not be empty",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}