package com.paugo.tmtimer.timer;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

/**
 * A timer writing its time on a TextView and able to notify a listener after specifics events.
 */
public class TMTimer {

    private static final String TAG = "TMTimer";

    private CountDownTimer countDownTimer;
    private long initTimeLeft;
    private long timeLeft;
    private TextView textViewTimer;
    private TMTimerListener listener;

    public TMTimer(long millisInFuture, TextView textViewTimer, TMTimerListener listener) {
        this.initTimeLeft = millisInFuture;
        this.timeLeft = millisInFuture;
        this.textViewTimer = textViewTimer;
        this.listener = listener;

        initializeCountDownTimer(millisInFuture);
    }

    private void initializeCountDownTimer(long ms) {
        this.countDownTimer = new CountDownTimer(ms, TMTimerUtil.TIMER_REFRESH_INTERVAL_MS) {
            @Override
            public void onTick(long millisUntilFinished) {
                TMTimer.this.timeLeft = millisUntilFinished;
                writeTime();

                if (millisUntilFinished <= TMTimerUtil.CRITICAL_TIME_LEFT_MS) {
                    TMTimer.this.listener.onCriticalLimit();
                }
            }

            @Override
            public void onFinish() {
                TMTimer.this.timeLeft = 0;
                writeTime();
                TMTimer.this.listener.onTimeout();
            }
        };

        writeTime();
    }

    public void pause() {
        Log.i(TAG, "Timer paused with " + this.timeLeft + " ms left on " + this.initTimeLeft + " ms initially allowed.");

        stop();
        initializeCountDownTimer(this.timeLeft);
    }

    public void resume() {
        start();
    }

    public void start() {
        this.countDownTimer.start();
    }

    public void stop() {
        this.countDownTimer.cancel();
    }

    public void reset() {
        this.timeLeft = this.initTimeLeft;
        initializeCountDownTimer(this.initTimeLeft);
    }

    public void writeTime() {
        TMTimerUtil.writeTime(this.timeLeft, this.textViewTimer);
    }

    public long getTimeLeft() {
        return timeLeft;
    }
}
