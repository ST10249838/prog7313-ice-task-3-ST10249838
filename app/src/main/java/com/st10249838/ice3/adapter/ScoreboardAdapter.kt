package com.st10249838.ice3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.st10249838.ice3.model.Score

class ScoreboardAdapter(private val scores: List<Score>) : RecyclerView.Adapter<ScoreboardAdapter.ScoreViewHolder>() {
    inner class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRank: TextView = itemView.findViewById(R.id.tvRank)
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val tvScore: TextView = itemView.findViewById(R.id.tvScore)
        val tvTimeTaken: TextView = itemView.findViewById(R.id.tvTimeTaken)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.tvRank.text = (position + 1).toString()
        holder.tvUsername.text = score.username
        holder.tvScore.text = "Score: ${score.score} / ${score.totalQuestions}"
        holder.tvTimeTaken.text = "Time: ${score.timeTaken / 1000} sec"
    }

    override fun getItemCount(): Int = scores.size
}
