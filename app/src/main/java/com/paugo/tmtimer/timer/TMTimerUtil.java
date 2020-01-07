package com.paugo.tmtimer.timer;

import android.graphics.Color;
import android.widget.TextView;

/**
 * An utility class to manage the timer.
 */
public final class TMTimerUtil {

    protected static final long TIMER_REFRESH_INTERVAL_MS = 1000;
    protected static final long WARNING_TIME_LEFT_MS = 60000;
    protected static final long CRITICAL_TIME_LEFT_MS = 20000;

    private TMTimerUtil() {
    }

    /**
     * Write the time in a TextView in a mm:ss format. It also change the timer color depending on time left (based on WARNING_TIME_LEFT_MS and CRITICAL_TIME_LEFT_MS values)
     *
     * @param ms            Time to show in milliseconds
     * @param textViewTimer TextView to be updated
     */
    public static void writeTime(long ms, TextView textViewTimer) {
        long seconds = (ms / 1000) % 60;
        long minutes = (ms / 60000);

        String formattedTime = String.format("%02d:%02d", Math.round(minutes), Math.round(seconds));
        textViewTimer.setText(formattedTime);
        if (ms <= CRITICAL_TIME_LEFT_MS) {
            textViewTimer.setTextColor(Color.RED);
        } else if (ms <= WARNING_TIME_LEFT_MS) {
            textViewTimer.setTextColor(Color.parseColor("#F06D2F"));
        } else {
            textViewTimer.setTextColor(Color.WHITE);
        }
    }

    public static boolean isInCriticalPeriod(long ms) {
        return ms <= CRITICAL_TIME_LEFT_MS;
    }
}
