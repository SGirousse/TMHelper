package com.paugo.tmhelper.timer;

/**
 * Interface for an activity listening a timer.
 */
public interface TMTimerListener {
    /**
     * Timer time out occurred.
     */
    void onTimeout();

    void onCriticalLimit();
}
