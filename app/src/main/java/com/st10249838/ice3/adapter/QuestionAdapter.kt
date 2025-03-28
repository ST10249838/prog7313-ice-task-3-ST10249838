package com.st10249838.ice3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.st10249838.ice3.model.Question
import com.st10249838.ice3.databinding.ItemQuestionBinding

class QuestionAdapter(
    private val questions: List<Question>,
    private val answerListener: OnAnswerSelectedListener
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    interface OnAnswerSelectedListener {
        fun onAnswerSelected(isCorrect: Boolean)
    }

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.tvQuestion.text = question.questionText
            // Clear previous text if reusing views
            binding.etAnswer.text.clear()
            binding.btnSubmit.isEnabled = true

            binding.btnSubmit.setOnClickListener {
                val userAnswer = binding.etAnswer.text.toString().trim()
                // Optionally, you can ignore case by converting both strings to lowercase.
                val isCorrect =
                    userAnswer.equals(question.correctAnswer.trim(), ignoreCase = true)
                binding.btnSubmit.isEnabled = false // prevent multiple clicks
                answerListener.onAnswerSelected(isCorrect)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding =
            ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size
}