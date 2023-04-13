package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val exitButton = findViewById<Button>(R.id.exit_btn)
        exitButton.setOnClickListener {finishAffinity()}

        val startGameBtn = findViewById<Button>(R.id.start_game_btn)
        startGameBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val statisticBtn = findViewById<Button>(R.id.statistic_btn)
        statisticBtn.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
    }

}