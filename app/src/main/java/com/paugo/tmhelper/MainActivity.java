package com.paugo.tmhelper;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.paugo.tmhelper.activity.options.GameOptionsSelectionActivity;
import com.paugo.tmhelper.application.TMTimerApplicationUtil;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer backgroundSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.backgroundSound = TMTimerApplicationUtil.initializeSound(MainActivity.this, R.raw.sd_scifi_ambient_theexpanse);
    }

    @Override
    public void onResume() {
        super.onResume();
        TMTimerApplicationUtil.manageSoundButtonAppearance(findViewById(R.id.main_soundButton));
        TMTimerApplicationUtil.playSound(this.backgroundSound);
        findViewById(R.id.main_startNewGameButton).setEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.backgroundSound.pause();
    }

    public void onSoundButton(View view) {
        TMTimerApplicationUtil.manageSoundButtonClick(view);
        TMTimerApplicationUtil.playSound(this.backgroundSound);
    }

    public void onStartNewGame(View view) {
        Intent intent = new Intent(this, GameOptionsSelectionActivity.class);
        startActivity(intent);
    }
}
