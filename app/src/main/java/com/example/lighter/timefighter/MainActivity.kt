package com.example.lighter.timefighter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal lateinit var tapMeButton: Button

    internal var score = 0;

    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal var initialCountDown: Long = 60000
    internal var countDownInterval: Long = 1000
    internal var timeLeft = 60

    internal val TAG = MainActivity::class.java.simpleName


    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called. Score is: $score")

        gameScoreTextView = findViewById<TextView>(R.id.game_score_text_view)
        timeLeftTextView = findViewById<TextView>(R.id.time_left_text_view)
        tapMeButton = findViewById<Button>(R.id.tap_me_button)

        tapMeButton.setOnClickListener { v ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
            v.startAnimation(bounceAnimation)
            incrementScore()
        }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveINstanceState: Saving Score: $score & Time Left: $timeLeft")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy called.")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            showInfo()
        }

        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    private fun restoreGame() {
        Log.d(TAG, "restoreGame called")

        val restoredScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = restoredScore

        val restoreTime = getString(R.string.time_left, Integer.toString(timeLeft))
        timeLeftTextView.text = restoreTime

        countDownTimer = object : CountDownTimer((timeLeft * 1000).toLong(), countDownInterval) {
            override fun onTick(millisUntiFinished: Long) {
                timeLeft = millisUntiFinished.toInt() / 1000

                val timeLeftString = getString(R.string.time_left, Integer.toString(timeLeft))
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted = true
    }

    private fun incrementScore() {
        score++

        val newScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = newScore

        if (!gameStarted) {
            startGame()
        }
    }

    private fun resetGame() {
        score = 0

        val initialScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = initialScore


        val initialTimeLeft = getString(R.string.time_left, Integer.toString(60))
        timeLeftTextView.text = initialTimeLeft

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.time_left, Integer.toString(timeLeft))
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message, Integer.toString(score)), Toast.LENGTH_LONG).show()
        resetGame()
    }
}
