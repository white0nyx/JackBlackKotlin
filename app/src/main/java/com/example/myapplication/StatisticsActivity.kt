package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class StatisticsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val text_wins = findViewById<TextView>(R.id.wins_score)
        val text_losess = findViewById<TextView>(R.id.losess_score)
        val text_procent_wins = findViewById<TextView>(R.id.procent_wins)
        val return_btn = findViewById<Button>(R.id.return_back)
        val reset_btn = findViewById<Button>(R.id.reset)
        val text_drows = findViewById<TextView>(R.id.drows_score)

        val wins = StatsManager.getWins(this)
        val losses = StatsManager.getLosses(this)
        val procent_of_wins = StatsManager.getWinPercentage(this)
        val draw_score = StatsManager.getDraws(this)

        text_wins.text = "Победы: $wins"
        text_losess.text = "Поражения: $losses"
        text_drows.text = "Ничьи: $draw_score"
        text_procent_wins.text = "Процент побед: $procent_of_wins%"



        reset_btn.setOnClickListener { StatsManager.resetStats(this);
            text_wins.text="Победы: 0"; text_losess.text="Поражения: 0"; text_drows.text = "Ничьи: 0";

            text_procent_wins.text="Процент побед: 0%"}

        return_btn.setOnClickListener { finish() }

    }
}