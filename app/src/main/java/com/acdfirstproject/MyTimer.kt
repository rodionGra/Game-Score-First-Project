package com.acdfirstproject

import com.ankushgrover.hourglass.Hourglass

class MyTimer(
    private val onTick: ((Long) -> Unit)? = null,
    private val onFinish: (() -> Unit)? = null,
    millis: Long = 60_000L,
    interval: Long = 1_000L
) : Hourglass(millis, interval) {

    override fun onTimerTick(timeRemaining: Long) {
        onTick?.invoke(timeRemaining)
    }

    override fun onTimerFinish() {
        onFinish?.invoke()
    }
}