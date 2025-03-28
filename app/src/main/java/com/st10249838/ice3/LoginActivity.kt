package com.st10249838.ice3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.st10249838.ice3.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LandingActivity::class.java).apply { putExtra("username", username) }
                            startActivity(intent)
                            finish()
                        } else {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { registerTask ->
                                    if (registerTask.isSuccessful) {
                                        Toast.makeText(this, "Registration Successful.", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, LandingActivity::class.java).apply { putExtra("username", username) }
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}