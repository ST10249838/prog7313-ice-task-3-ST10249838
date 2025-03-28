package com.st10249838.ice3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.st10249838.ice3.adapter.QuestionAdapter
import com.st10249838.ice3.databinding.ActivityMainBinding
import com.st10249838.ice3.model.Question
import com.st10249838.ice3.model.Score

class MainActivity : AppCompatActivity(), QuestionAdapter.OnAnswerSelectedListener {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val username: String? by lazy { intent.getStringExtra("username") }
    private lateinit var questionAdapter: QuestionAdapter
    private val questionsList = mutableListOf<Question>()
    private var currentQuestionIndex = 0
    private var totalScore = 0
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        startTime = System.currentTimeMillis()
        setupRecyclerView()
        val category = intent.getStringExtra("category") ?: "Error"
        loadQuestions(category)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvQuestions.layoutManager = layoutManager
        PagerSnapHelper().attachToRecyclerView(binding.rvQuestions)

        questionAdapter = QuestionAdapter(questionsList, this)
        binding.rvQuestions.adapter = questionAdapter
    }

    // Get the questions from the database
    private fun loadQuestions(category: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("QuestionsDb")
            .document(category)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data: Map<String, Any>? = documentSnapshot.data
                    data?.forEach { (key, value) ->
                        val question = Question(questionText = key, correctAnswer = value.toString())
                        questionsList.add(question)
                    }
                    questionAdapter.notifyDataSetChanged()
                    binding.seekBar.max = questionsList.size
                    updateProgress()
                } else {
                    println("Document does not exist!")
                }
            }
            .addOnFailureListener { exception ->
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Failed to load questions: ${exception.message}")
                    .setPositiveButton("OK", null)
                    .show()
            }
    }

    // When an answer is chosen
    override fun onAnswerSelected(isCorrect: Boolean) {
        if (isCorrect) {
            totalScore++
        }
        currentQuestionIndex++
        updateProgress()

        if (currentQuestionIndex < questionsList.size) {
            binding.rvQuestions.smoothScrollToPosition(currentQuestionIndex)
        } else {
            showFinalScore()
        }
    }

    // Update the progress of the quiz using the seek bar
    private fun updateProgress() {
        binding.seekBar.setProgress(currentQuestionIndex, true)
    }

    // Method for when test is completed
    private fun showFinalScore() {
        val finishTime = System.currentTimeMillis()
        val timeTakenMillis = finishTime - startTime

        val scoreData = hashMapOf(
            "username" to username,
            "score" to totalScore,
            "totalQuestions" to questionsList.size,
            "timeTaken" to timeTakenMillis,
            "timestamp" to FieldValue.serverTimestamp()
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("Scoreboard")
            .add(scoreData)
            .addOnSuccessListener {
                AlertDialog.Builder(this)
                    .setTitle("Quiz Completed")
                    .setMessage("Your total score is: $totalScore out of ${questionsList.size}\nTime Taken: ${timeTakenMillis / 1000} seconds")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }.show()
            }
            .addOnFailureListener { exception ->
                AlertDialog.Builder(this)
                    .setTitle("Quiz Completed")
                    .setMessage("Your total score is: $totalScore out of ${questionsList.size}\nTime Taken: ${timeTakenMillis / 1000} seconds\nError saving score: ${exception.message}")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }.show()
            }
    }
}