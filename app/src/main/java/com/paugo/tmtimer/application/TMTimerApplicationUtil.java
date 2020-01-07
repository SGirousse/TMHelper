package com.paugo.tmtimer.application;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.paugo.tmtimer.R;

public final class TMTimerApplicationUtil {

    public static final long DEFAULT_ALL_PLAYERS_CARD_BUY_TIME_IN_MS = 300000;
    public static final long DEFAULT_PLAYER_TURN_TIME_IN_MS = 300000;
    private static final String TAG = "TMTimerApplicationUtil";

    private TMTimerApplicationUtil() {
    }

    public static MediaPlayer initializeSound(Context context, int soundResourceId) {
        MediaPlayer sound = MediaPlayer.create(context, soundResourceId);

        // Start it once to load the resource and then set it with the right status
        sound.start();
        playSound(sound);

        return sound;
    }

    public static boolean playSound(MediaPlayer sound) {
        Log.d(TAG, "Sound id on ? " + TMTimerApplication.isSoundOn);

        if (sound != null) {
            if (TMTimerApplication.isSoundOn) {
                sound.start();
            } else {
                sound.pause();
            }
            return TMTimerApplication.isSoundOn;
        } else {
            Log.e(TAG, "The sound is NULL and cannot be managed.");
        }

        return false;
    }

    public static void manageSoundButtonClick(View view) {
        TMTimerApplication.isSoundOn = !TMTimerApplication.isSoundOn;
        manageSoundButtonAppearance(view);
    }

    public static void manageSoundButtonAppearance(View view) {
        if (TMTimerApplication.isSoundOn) {
            ((ImageButton) view).setImageResource(R.drawable.ic_sound_on);
        } else {
            ((ImageButton) view).setImageResource(R.drawable.ic_sound_off);
        }
    }
}
