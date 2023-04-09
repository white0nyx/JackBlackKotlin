package com.example.myapplication

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stop_game_btn = findViewById<ImageButton>(R.id.stop_game_btn)
        val more_btn = findViewById<Button>(R.id.button_more)
        val stop_btn = findViewById<Button>(R.id.button_stop)

        stop_game_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Вы уверены, что хотите покинуть игру? Выход из игры будет засчитан, как поражение.")
                .setCancelable(false)
                .setPositiveButton("Да") { dialog, id -> finish() }
                .setNegativeButton("Нет", null)
            val alert = builder.create()
            alert.show()
        }


        val cards_powers = mapOf(
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9,
            "10" to 10,
            "J" to 10,
            "Q" to 10,
            "K" to 10,
            "A" to 11,
        )

        val cards = mutableListOf(
            "2♠", "2♥", "2♣", "2♦",
            "3♠", "3♥", "3♣", "3♦",
            "4♠", "4♥", "4♣", "4♦",
            "5♠", "5♥", "5♣", "5♦",
            "6♠", "6♥", "6♣", "6♦",
            "7♠", "7♥", "7♣", "7♦",
            "8♠", "8♥", "8♣", "8♦",
            "9♠", "9♥", "9♣", "9♦",
            "10♠", "10♥", "10♣", "10♦",
            "J♠", "J♥", "J♣", "J♦",
            "Q♠", "Q♥", "Q♣", "Q♦",
            "K♠", "K♥", "K♣", "K♦",
            "A♠", "A♥", "A♣", "A♦",
        )

        val shuffled_cards = cards.shuffled().toMutableList()
        val text_view = findViewById<TextView>(R.id.textView)

        var diller_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
        shuffled_cards.removeAt(0)
        shuffled_cards.removeAt(0)
        var player_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
        shuffled_cards.removeAt(0)
        shuffled_cards.removeAt(0)

        var diller_points = 0
        var player_points = 0


        text_view.text = "Карты диллера: $diller_cards\nОчков у диллера: $diller_points\n\n" +
                "Ваши карты: $player_cards \nОчков у вас: $player_points\n\n Оставшиеся карты:"
        text_view.append("$shuffled_cards")


        fun more_card(shuffled_cards: MutableList<String>){
            var rand_card = shuffled_cards[0]
            shuffled_cards.removeAt(0)
            player_cards.add(rand_card)

            text_view.text = "Карты диллера: $diller_cards\nОчков у диллера: $diller_points\n\n" +
                    "Ваши карты: $player_cards \nОчков у вас: $player_points\n\n Оставшиеся карты:"
            text_view.append("$shuffled_cards")
        }

        more_btn.setOnClickListener { more_card(shuffled_cards) }
    }

}