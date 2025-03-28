package com.st10249838.ice3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.st10249838.ice3.databinding.ActivityScoreboardBinding
import com.st10249838.ice3.model.Score

class ScoreboardActivity : AppCompatActivity() {

    private val binding: ActivityScoreboardBinding by lazy { ActivityScoreboardBinding.inflate(layoutInflater) }
    private lateinit var scoreboardAdapter: ScoreboardAdapter
    private val scoresList = mutableListOf<Score>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        loadScores()
    }

    private fun setupRecyclerView() {
        binding.rvScores.layoutManager = LinearLayoutManager(this)
        scoreboardAdapter = ScoreboardAdapter(scoresList)
        binding.rvScores.adapter = scoreboardAdapter
    }

    private fun loadScores() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Scoreboard")
            .orderBy("score", Query.Direction.DESCENDING)
            .orderBy("timeTaken", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                scoresList.clear()
                snapshot.documents.forEach { document ->
                    val score = document.toObject(Score::class.java)
                    score?.let { scoresList.add(it) }
                }
                scoreboardAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error loading scoreboard: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
