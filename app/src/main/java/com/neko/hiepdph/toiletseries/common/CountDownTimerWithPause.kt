package com.neko.hiepdph.toiletseries.common

import android.os.CountDownTimer

abstract class CountDownTimerWithPause(var mMillisInFuture: Long, var mInterval: Long) {

    private var countDownTimer: CountDownTimer? = null
    private var remainingTime: Long = 0
    private var isTimerPaused: Boolean = true
    private var TAG: String = "Timer Service"

    init {
        this.remainingTime = mMillisInFuture
    }

    fun setRemainTime(time: Long) {
        this.remainingTime = time
        countDownTimer?.cancel()
        isTimerPaused = true
        start()
    }

    @Synchronized
    fun start() {
        if (isTimerPaused) {
            countDownTimer = object : CountDownTimer(remainingTime, mInterval) {
                override fun onFinish() {
                    onTimerFinish()
                    restart()
                }

                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished
                    onTimerTick(millisUntilFinished)
                }

            }.apply {
                start()
            }
            isTimerPaused = false
        }
    }

    fun pause() {
        if (!isTimerPaused) {
            countDownTimer?.cancel()

        }
        isTimerPaused = true
    }

    fun restart() {
        countDownTimer?.cancel()
        remainingTime = mMillisInFuture
        isTimerPaused = true
    }

    fun cancel() {
        countDownTimer?.cancel()
    }

    abstract fun onTimerTick(millisUntilFinished: Long)

    abstract fun onTimerFinish()
}