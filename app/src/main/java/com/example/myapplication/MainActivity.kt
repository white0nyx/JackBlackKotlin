package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.round


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stop_game_btn = findViewById<ImageButton>(R.id.stop_game_btn)
        val more_btn = findViewById<Button>(R.id.button_more)
        val stop_btn = findViewById<Button>(R.id.button_stop)

        stop_game_btn.setOnClickListener {
            StatsManager.incrementLosses(this)
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



//        ДЛЯ ТЕСТОВ
//        val shuffled_cards = mutableListOf("A♠", "9♠", "A♥", "9♥", "A♥", "3♥", "3♣")

        val text_view = findViewById<TextView>(R.id.textView)


        val shuffled_cards = cards.shuffled().toMutableList()
        var diller_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
        shuffled_cards.removeAt(0)
        shuffled_cards.removeAt(0)
        var player_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
        shuffled_cards.removeAt(0)
        shuffled_cards.removeAt(0)


//        fun get_points(cards: MutableList<String>, count_points_now: Int): Int {
//            var total_points = 0
//            for (card in cards) {
//                val base_card = card.substring(0, card.length - 1)
//
//                if (base_card == "A" && count_points_now >= 10) {
//                    total_points += 1
//
//                } else {
//                    val card_power = cards_powers[base_card]
//                    if (card_power != null) {
//                        total_points += card_power
//                    }
//                }
//
//            }
//
//            return total_points
//        }

        fun get_points(cards: MutableList<String>): Int {
            var points = 0
            var aces = 0
            for (card in cards) {
                val card_base = card.substring(0, card.length - 1)
                if (card_base == "A") {
                    aces += 1
                    points += 11
                } else {
                    points += cards_powers[card_base] ?: 0
                }
            }
            while (points > 21 && aces > 0) {
                points -= 10
                aces -= 1
            }
            return points
        }

        var diller_points = 0
        var player_points = 0

        player_points = get_points(player_cards)
        diller_points = get_points(diller_cards)

        var is_fast_diller_win = false
        if (diller_points == 21) {

            is_fast_diller_win = true
//            val builder = AlertDialog.Builder(this)
//            builder.setMessage("Вы проиграли").setPositiveButton("ОК") { dialog, id -> finish() }
//            val alert = builder.create()
//            alert.show()
        }


        text_view.text = "Карты диллера: ## ${
            diller_cards.subList(
                1,
                diller_cards.lastIndex + 1
            )
        }\nОчков у диллера: $diller_points\n\n" +
                "Ваши карты: $player_cards \nОчков у вас: $player_points\n\n Оставшиеся карты:"
        text_view.append("${shuffled_cards}")


        fun reset_game() {

            val shuffled_cards = cards.shuffled().toMutableList()
            diller_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
            shuffled_cards.removeAt(0)
            shuffled_cards.removeAt(0)
            player_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
            shuffled_cards.removeAt(0)
            shuffled_cards.removeAt(0)

            player_points = get_points(player_cards)
            diller_points = get_points(diller_cards)

            text_view.text = "Карты диллера: ## ${
                diller_cards.subList(1, diller_cards.lastIndex + 1)}\nОчков у диллера: $diller_points\n\n" +
                    "Ваши карты: $player_cards \nОчков у вас: $player_points\n\n Оставшиеся карты:"
            text_view.append("$shuffled_cards")

        }


        fun more_card(shuffled_cards: MutableList<String>) {
//            var diller_points_now = get_points(diller_cards)
//            Log.d("Ошибка", "$diller_points_now")
//            while (diller_points_now < 17) {
//                diller_cards.add(shuffled_cards[0])
//                shuffled_cards.removeAt(0)
//                diller_points_now = get_points(diller_cards)
//            }

            player_cards.add(shuffled_cards[0])
            shuffled_cards.removeAt(0)

            player_points = get_points(player_cards)
            diller_points = get_points(diller_cards)



            text_view.text = "Карты диллера: ## ${
                diller_cards.subList(1, diller_cards.lastIndex + 1)}\nОчков у диллера: $diller_points\n\n" +
                    "Ваши карты: $player_cards \nОчков у вас: $player_points\n\n Оставшиеся карты:"
            text_view.append("$shuffled_cards")

            if (player_points > 21) {

                text_view.text = "Карты диллера: ${diller_cards}\nОчков у диллера: $diller_points\n\n" +
                        "Ваши карты: $player_cards \nОчков у вас: $player_points\n\n Оставшиеся карты:"
                text_view.append("$shuffled_cards")

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Вы проиграли!")
                    .setCancelable(false)
                    .setNegativeButton("Выйти", { dialog, id -> finish() })
                    .setPositiveButton("Новая игра",  { dialog, id -> reset_game() })

                val alert = builder.create()
                alert.show()
            }
        }
        fun enough() {

            var diller_points = get_points(diller_cards)
            Log.d("Ошибка", "$diller_points")
            while (diller_points < 17) {
                diller_cards.add(shuffled_cards[0])
                shuffled_cards.removeAt(0)
                diller_points = get_points(diller_cards)
            }
            Log.d("Ошибка", "$diller_points")
            if ((diller_points <= 21) && (diller_points > player_points)) {
                StatsManager.incrementLosses(this)
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Вы проиграли!")
                    .setCancelable(false)
                    .setNegativeButton("Выйти", { dialog, id -> finish() })
                    .setPositiveButton("Новая игра",  { dialog, id -> reset_game() })

                val alert = builder.create()
                alert.show()
            }

            else {
                StatsManager.incrementWins(this)
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Вы победили!")
                    .setCancelable(false)
                    .setNegativeButton("Выйти", { dialog, id -> finish() })
                    .setPositiveButton("Новая игра",  { dialog, id -> reset_game() })

                val alert = builder.create()
                alert.show()
            }

            text_view.text = "Карты диллера: ${diller_cards}\nОчков у диллера: $diller_points\n\n" +
                    "Ваши карты: $player_cards \nОчков у вас: $player_points\n\n Оставшиеся карты:"
            text_view.append("$shuffled_cards")
        }



        more_btn.setOnClickListener { more_card(shuffled_cards) }
        stop_btn.setOnClickListener { enough() }

    }


}

object StatsManager {
    private const val PREFS_NAME = "MyPrefs"
    private const val KEY_WINS = "wins"
    private const val KEY_LOSSES = "losses"

    fun incrementWins(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val wins = prefs.getInt(KEY_WINS, 0)
        prefs.edit().putInt(KEY_WINS, wins + 1).apply()
    }

    fun incrementLosses(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val losses = prefs.getInt(KEY_LOSSES, 0)
        prefs.edit().putInt(KEY_LOSSES, losses + 1).apply()
    }

    fun getWins(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_WINS, 0)
    }

    fun getLosses(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_LOSSES, 0)
    }


    fun getWinPercentage(context: Context): Int {
        val wins = getWins(context)
        val losses = getLosses(context)
        val totalGames = wins + losses

        return if (totalGames > 0) {
            round((wins.toFloat() / totalGames.toFloat()) * 100).toInt()
        } else {
            0
        }
    }

    fun resetStats(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}


