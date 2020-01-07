package com.paugo.tmhelper.activity.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paugo.tmhelper.R;
import com.paugo.tmhelper.application.TMTimerApplicationUtil;
import com.paugo.tmhelper.timer.TMTimer;
import com.paugo.tmhelper.timer.TMTimerListener;
import com.paugo.tmhelper.timer.TMTimerUtil;

import java.util.Timer;
import java.util.TimerTask;

public class InterGenActivity extends AppCompatActivity implements TMTimerListener {

    private static final String TAG = "InterGenActivity";

    private static final long BUTTON_DISABLE_TIME_MS = 3000;

    private long generation;

    private TMTimer timer;
    private MediaPlayer playerAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_gen);

        Intent intent = getIntent();

        this.generation = intent.getIntExtra(PlayerTimerActivity.CURRENT_GENERATION_NUMBER, 0);
        ((TextView) findViewById(R.id.textViewEndGenTitle)).setText("Fin de la génération " + this.generation);

        this.timer = new TMTimer(intent.getLongExtra(PlayerTimerActivity.ALL_PLAYERS_CARD_BUY_TIME_IN_MS, 180000), ((TextView) findViewById(R.id.chronoCarBuy)), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        TMTimerApplicationUtil.manageSoundButtonAppearance(findViewById(R.id.interGen_soundButton));
        if (this.timer != null && TMTimerUtil.isInCriticalPeriod(this.timer.getTimeLeft())) {
            TMTimerApplicationUtil.playSound(this.playerAlert);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.playerAlert != null) {
            this.playerAlert.pause();
        } else {
            Log.w(TAG, "The player alert sound has not been initialized");
        }
    }

    public void onStartBuyCards(View view) {
        view.setEnabled(false);
        this.timer.start();
        this.playerAlert = TMTimerApplicationUtil.initializeSound(InterGenActivity.this, R.raw.sd_scifi_alarm_nuclear);
    }

    public void onNextGeneration(View view) {
        view.setEnabled(false);
        final Button button = (Button) view;
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setEnabled(true);
                    }
                });
            }
        }, BUTTON_DISABLE_TIME_MS);

        startNextGeneration();
    }

    public void startNextGeneration() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Souhaitez-vous démarrer la génération " + (this.generation + 1))
                .setTitle("Nouvelle génération")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (InterGenActivity.this.playerAlert != null) {
                            InterGenActivity.this.playerAlert.stop();
                        }
                        if (InterGenActivity.this.timer != null) {
                            InterGenActivity.this.timer.stop();
                        }
                        InterGenActivity.this.finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onSoundButton(View view) {
        TMTimerApplicationUtil.manageSoundButtonClick(view);
        TMTimerApplicationUtil.playSound(this.playerAlert);
    }

    @Override
    public void onTimeout() {
        startNextGeneration();
        if (this.playerAlert != null) {
            this.playerAlert.stop();
        }
    }

    @Override
    public void onCriticalLimit() {
        TMTimerApplicationUtil.playSound(this.playerAlert);
    }

    @Override
    public void onBackPressed() {
        startNextGeneration();
    }
}
