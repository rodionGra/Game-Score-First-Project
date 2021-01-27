package com.acdfirstproject

import android.os.CountDownTimer

class CustomTimer(
    millisInFuture: Long,
    private val countDownInterval: Long,
    private var onTick: ((millisUntilFinished: Long) -> Unit)? = null,
    private var onFinish: (() -> Unit)? = null
) {

    private var millisUntilFinished: Long = millisInFuture
    private var timer = InternalTimer(this, millisInFuture, countDownInterval)

    var isRunning = false
        private set

    private class InternalTimer(
        private val parent: CustomTimer,
        millisInFuture: Long,
        countDownInterval: Long
    ) : CountDownTimer(millisInFuture, countDownInterval) {

        var millisUntilFinished: Long = parent.millisUntilFinished

        override fun onFinish() {
            millisUntilFinished = 0
            parent.onFinish?.invoke()
        }

        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            parent.onTick?.invoke(millisUntilFinished)
        }
    }

    fun pause() {
        if (isRunning) {
            timer.cancel()
            isRunning = false
        }
    }

    fun resume() {
        if (!isRunning && timer.millisUntilFinished > 0) {
            timer = InternalTimer(this, timer.millisUntilFinished, countDownInterval)
            start()
        }
    }

    fun start() {
        timer.start()
        isRunning = true
    }

    fun stop() {
        this.timer.cancel()
    }
}