package com.example.countdowntimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import java.text.DecimalFormat
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var timeTxt: TextView
    private lateinit var circularProgressBar: ProgressBar
    private lateinit var linearProgressBar: ProgressBar

    private val countdownTime = 3600 // 1 hour , 3600 second, 60 min
    private val clockTime = (countdownTime * 1000).toLong()
    private val progressTime = (clockTime / 1000).toFloat()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressedMethod()
        }

    }


    private lateinit var customCountdownTimer: CustomCountdownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        timeTxt = findViewById(R.id.timeTxt)
        circularProgressBar = findViewById(R.id.circularProgressBar)
        linearProgressBar = findViewById(R.id.linearProgressBar)

        var secondsLeft = 0
        customCountdownTimer = object : CustomCountdownTimer(clockTime, 1000) {}
        customCountdownTimer.onTick = { millisUntilFinished ->
            val second = (millisUntilFinished / 1000.0f).roundToInt()
            if (second != secondsLeft) {
                secondsLeft = second

                timerFormat(
                    secondsLeft,
                    timeTxt
                )
            }
        }
        customCountdownTimer.onFinish = {
            timerFormat(
                0,
                timeTxt
            )
        }
        circularProgressBar.max = progressTime.toInt()
        linearProgressBar.max = progressTime.toInt()

        circularProgressBar.progress = progressTime.toInt()
        linearProgressBar.progress = progressTime.toInt()

        customCountdownTimer.startTimer()


        val pauseBtn = findViewById<Button>(R.id.pauseBtn)
        val resumeBtn = findViewById<Button>(R.id.resumeBtn)
        val resetBtn = findViewById<Button>(R.id.resetBtn)


        pauseBtn.setOnClickListener {
            customCountdownTimer.pauseTimer()

        }

        resumeBtn.setOnClickListener {
            customCountdownTimer.resumeTimer()
        }

        resetBtn.setOnClickListener {
            circularProgressBar.progress = progressTime.toInt()
            linearProgressBar.progress = progressTime.toInt()
            customCountdownTimer.restartTimer()
        }

    }

    private fun timerFormat(secondsLeft: Int, timeTxt: TextView) {
        linearProgressBar.progress = secondsLeft
        circularProgressBar.progress = secondsLeft
        val decimalFormat = DecimalFormat("00")
        val hour = secondsLeft / 3600
        val min = (secondsLeft % 3600) / 60
        val seconds = secondsLeft % 60

        val timeFormat1 = decimalFormat.format(secondsLeft)
        val timeFormat2 = decimalFormat.format(min) + ":" + decimalFormat.format(seconds)
        val timeFormat3 =
            decimalFormat.format(hour) + ":" + decimalFormat.format(min) + ":" + decimalFormat.format(
                seconds
            )

        timeTxt.text = timeFormat1 + "\n" + timeFormat2 + "\n" + timeFormat3
    }

    private fun onBackPressedMethod() {
        customCountdownTimer.destroyTimer()
        finish()
    }

    override fun onPause() {
        customCountdownTimer.pauseTimer()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        customCountdownTimer.resumeTimer()
    }

    override fun onDestroy() {
        customCountdownTimer.destroyTimer()
        super.onDestroy()
    }

}