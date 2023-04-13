package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.round


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n", "MissingInflatedId")
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

        fun Int.dpToPx(): Int {
            val metrics = Resources.getSystem().displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
        }

        val diller_cards_layout = findViewById<LinearLayout>(R.id.diller_cards_layout)
        val player_cards_layout = findViewById<LinearLayout>(R.id.player_cards_layout)

        fun create_card(value: String, who: String, hide: Boolean) {
            val button = Button(this).apply {
                layoutParams = ViewGroup.MarginLayoutParams(83.dpToPx(), 113.dpToPx()).apply {
                    setMargins(8.dpToPx(), 0, 0, 0)
                }

                if (hide) {
                    setBackgroundResource(R.drawable.card_background_hide)
                }

                else {
                    setBackgroundResource(R.drawable.card_background_open)
                    this.text = value

                }
                setTextColor(Color.BLACK)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f)
            }

            if (who == "diller") {
                diller_cards_layout.addView(button)
            }

            else if (who == "player") {
                player_cards_layout.addView(button)
            }


        }

//        ДЛЯ ТЕСТОВ
//        val shuffled_cards = mutableListOf("A♠", "9♠", "A♥", "9♥", "A♥", "3♥", "3♣")



        val shuffled_cards = cards.shuffled().toMutableList()
        var diller_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
        create_card(diller_cards[0], "diller", true)
        create_card(diller_cards[1], "diller", false)
        shuffled_cards.removeAt(0)
        shuffled_cards.removeAt(0)
        var player_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
        create_card(player_cards[0], "player", false)
        create_card(player_cards[1], "player", false)
        shuffled_cards.removeAt(0)
        shuffled_cards.removeAt(0)


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

        var diller_points = get_points(diller_cards.subList(1, diller_cards.lastIndex + 1))
        var player_points = get_points(player_cards)

        val diller_points_text = findViewById<TextView>(R.id.diller_points_text)
        val player_points_text = findViewById<TextView>(R.id.player_points_text)
        diller_points_text.text = "Очки диллера: $diller_points"
        player_points_text.text = "Ваши очки: $player_points"


        fun reset_game() {

            val shuffled_cards = cards.shuffled().toMutableList()
            diller_cards_layout.removeAllViews()
            player_cards_layout.removeAllViews()
            diller_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
            shuffled_cards.removeAt(0)
            shuffled_cards.removeAt(0)
            create_card(diller_cards[0], "diller", true)
            create_card(diller_cards[1], "diller", false)
            player_cards = mutableListOf<String>(shuffled_cards[0], shuffled_cards[1])
            shuffled_cards.removeAt(0)
            shuffled_cards.removeAt(0)
            create_card(player_cards[0], "player", false)
            create_card(player_cards[1], "player", false)
            diller_points = get_points(diller_cards.subList(1, diller_cards.lastIndex + 1))
            player_points = get_points(player_cards)
            diller_points_text.text = "Очки диллера: $diller_points"
            player_points_text.text = "Ваши очки: $player_points"


        }


        fun more_card(shuffled_cards: MutableList<String>) {

            player_cards.add(shuffled_cards[0])
            create_card(shuffled_cards[0], "player", false)
            shuffled_cards.removeAt(0)

            player_points = get_points(player_cards)
            diller_points = get_points(diller_cards.subList(1, diller_cards.lastIndex + 1))
            diller_points_text.text = "Очки диллера: $diller_points"
            player_points_text.text = "Ваши очки: $player_points"


            if (player_points > 21) {
                diller_points = get_points(diller_cards)
                diller_points_text.text = "Очки диллера: $diller_points"

                diller_cards_layout.removeAllViews()
                for (diller_card in diller_cards) {
                    create_card(diller_card, "diller", false)
                }

                val builder = AlertDialog.Builder(this)
                StatsManager.incrementLosses(this)
                builder.setMessage("Вы проиграли!")
                    .setCancelable(false)
                    .setNegativeButton("Выйти", { dialog, id -> finish() })
                    .setPositiveButton("Новая игра",  { dialog, id -> reset_game() })

                val alert = builder.create()
                alert.show()
            }
        }

        fun enough() {

            diller_points = get_points(diller_cards)
            while (diller_points < 17) {
                diller_cards.add(shuffled_cards[0])
                create_card(shuffled_cards[1], "diller", false)
                shuffled_cards.removeAt(0)
                diller_points = get_points(diller_cards)
            }
            Log.d("Ошибка", "$diller_points")



            if ((diller_points <= 21) && (diller_points > player_points)) {
                val builder = AlertDialog.Builder(this)
                StatsManager.incrementLosses(this)
                builder.setMessage("Вы проиграли!")
                    .setCancelable(false)
                    .setNegativeButton("Выйти", { dialog, id -> finish() })
                    .setPositiveButton("Новая игра",  { dialog, id -> reset_game() })

                val alert = builder.create()
                alert.show()
            }

            else if (diller_points == player_points) {
                val builder = AlertDialog.Builder(this)
                StatsManager.incrementDraws(this)
                builder.setMessage("Ничья!")
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

            diller_cards_layout.removeAllViews()
            for (diller_card in diller_cards) {
                create_card(diller_card, "diller", false)
            }

            diller_points_text.text = "Очки диллера: $diller_points"
            player_points_text.text = "Ваши очки: $player_points"

        }


        Log.d("Очки диллера", "$diller_points")
        Log.d("Очки игрока", "$player_points")

        more_btn.setOnClickListener { more_card(shuffled_cards) }
        stop_btn.setOnClickListener { enough() }

    }


}

object StatsManager {
    private const val PREFS_NAME = "MyPrefs"
    private const val KEY_WINS = "wins"
    private const val KEY_LOSSES = "losses"
    private const val KEY_DRAWS = "draws"

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

    fun incrementDraws(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val draws = prefs.getInt(KEY_DRAWS, 0)
        prefs.edit().putInt(KEY_DRAWS, draws + 1).apply()
    }

    fun getDraws(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_DRAWS, 0)
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
        val draws = getDraws(context)
        val totalGames = wins + losses + draws

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


