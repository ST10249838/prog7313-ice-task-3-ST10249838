package com.st10249838.ice3.model

import com.google.firebase.Timestamp

data class Score(
    val username: String = "",
    val score: Long = 0,
    val totalQuestions: Long = 0,
    val timeTaken: Long = 0,
    val timestamp: Timestamp? = null
)
