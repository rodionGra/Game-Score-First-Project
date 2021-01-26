package com.acdfirstproject

import android.os.CountDownTimer
import kotlin.concurrent.timer

class MyTimer(private val onTick: (Long) -> (Unit), private val onFinish: () -> (Unit)) {

    var isRunning = true
        private set

    private lateinit var countDownTimer: CountDownTimer
    private var milliTillFinished: Long = 0

    fun startTimer(time_in_milliSeconds: Long) {
        isRunning = true
        countDownTimer = object : CountDownTimer(time_in_milliSeconds, 1000) {
            override fun onFinish() {
                onFinish.invoke()
            }
            override fun onTick(millisUntilFinished: Long) {
                milliTillFinished = millisUntilFinished
                onTick.invoke(millisUntilFinished)
            }
        }.start()
    }

    fun cancel() {
        this.countDownTimer.cancel()
    }

    fun continueTimer(){
        if (!this.isRunning) startTimer(milliTillFinished)
    }

    fun pauseTimer(){
        isRunning = false
        countDownTimer.cancel()
    }

    fun switchTimer() {
        if (this.isRunning) {
            pauseTimer()
        } else {
            continueTimer()
        }
    }
}