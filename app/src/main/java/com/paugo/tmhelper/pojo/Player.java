package com.paugo.tmhelper.pojo;

import android.util.Log;
import android.widget.TextView;

import com.paugo.tmhelper.timer.TMTimer;
import com.paugo.tmhelper.timer.TMTimerListener;

public class Player {
    private static final String TAG = "Player";

    private String name;
    private TextView playerTime;
    private TextView playerOrder;

    // For the current generation, amount of time left in ms for the player.
    private long playerTimeLeft;
    // For the current generation, tells if the player is still active or not.
    private boolean isActive;

    private TMTimer timer;
    private TMTimerListener tmTimerListener;

    public Player(String name, TextView playerTime, TextView playerOrder, long playerTimeLeft, TMTimerListener tmTimerListener) {
        Log.i(TAG, "Initialize player " + name + "with basic time = " + playerTimeLeft + " ms");
        this.name = name;
        this.playerTime = playerTime;
        this.playerOrder = playerOrder;
        this.playerTimeLeft = playerTimeLeft;

        this.tmTimerListener = tmTimerListener;

        this.timer = new TMTimer(this.playerTimeLeft, this.playerTime, this.tmTimerListener);

        resetPlayerForNewGeneration();
    }

    /**
     * When it is the player turns, initialize its timer (with time left) and directly write it.
     */
    public void startTurn() {
        Log.d(TAG, "Starting turn of : " + this.name);
        this.timer.resume();
    }

    /**
     * When a new generation starts, each player is active again and its time is restored.
     */
    public void resetPlayerForNewGeneration() {
        this.isActive = true;
        this.timer.reset();
    }

    public void goToNextPlayer() {
        this.timer.pause();
    }

    public void setTimerPaused(boolean isPaused) {
        if (isPaused) {
            this.timer.pause();
        } else {
            this.timer.resume();
        }
    }

    /**
     * When a player go to next generation, they becomes inactive for current generation.
     */
    public void goToNextGeneration() {
        this.isActive = false;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public TextView getPlayerOrder() {
        return playerOrder;
    }
}
