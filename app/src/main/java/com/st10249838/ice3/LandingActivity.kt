package com.st10249838.ice3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.st10249838.ice3.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {

    private val binding: ActivityLandingBinding by lazy { ActivityLandingBinding.inflate(layoutInflater) }
    private val username: String? by lazy { intent.getStringExtra("username") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnGeneralKnowledge.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("category", "General Knowledge")
            intent.putExtra("username", username)
            startActivity(intent)
        }
        
        binding.btnScienceTechnology.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("category", "Science & Technology")
            intent.putExtra("username", username)
            startActivity(intent)
        }
        
        binding.btnMoviesTV.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("category", "Movies & TV")
            intent.putExtra("username", username)
            startActivity(intent)
        }
        
        binding.btnSportsGames.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("category", "Sports & Games")
            intent.putExtra("username", username)
            startActivity(intent)
        }
        
        binding.btnScoreboard.setOnClickListener {
            val intent = Intent(this, ScoreboardActivity::class.java)
            startActivity(intent)
        }
    }
}