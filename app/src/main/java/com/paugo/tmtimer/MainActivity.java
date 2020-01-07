package com.paugo.tmtimer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.paugo.tmtimer.application.TMTimerApplicationUtil;

public class MainActivity extends AppCompatActivity {
    public static final String PLAYER_1_NAME = "PLAYER_1_NAME";
    public static final String PLAYER_2_NAME = "PLAYER_2_NAME";
    public static final String PLAYER_3_NAME = "PLAYER_3_NAME";
    public static final String PLAYER_4_NAME = "PLAYER_4_NAME";
    public static final String PLAYER_5_NAME = "PLAYER_5_NAME";
    public static final String PLAYER_TURN_TIME_IN_MS = "PLAYER_TURN_TIME_IN_MS";
    public static final String ALL_PLAYERS_CARD_BUY_TIME_IN_MS = "ALL_PLAYERS_CARD_BUY_TIME_IN_MS";
    private static final String TAG = "MainActivity";
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
        findViewById(R.id.main_startButton).setEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.backgroundSound.pause();
    }

    public void startGame(View view) {
        view.setEnabled(false);

        Intent intent = new Intent(this, PlayerTimerActivity.class);

        String player1Name = ((EditText) findViewById(R.id.main_editTextPlayerName1)).getText().toString();
        intent.putExtra(PLAYER_1_NAME, player1Name);
        String player2Name = ((EditText) findViewById(R.id.main_editTextPlayerName2)).getText().toString();
        intent.putExtra(PLAYER_2_NAME, player2Name);
        String player3Name = ((EditText) findViewById(R.id.main_editTextPlayerName3)).getText().toString();
        intent.putExtra(PLAYER_3_NAME, player3Name);
        String player4Name = ((EditText) findViewById(R.id.main_editTextPlayerName4)).getText().toString();
        intent.putExtra(PLAYER_4_NAME, player4Name);
        String player5Name = ((EditText) findViewById(R.id.main_editTextPlayerName5)).getText().toString();
        intent.putExtra(PLAYER_5_NAME, player5Name);

        String playerTurnTimeMin = ((EditText) findViewById(R.id.main_editTextPlayerTimeMin)).getText().toString();
        String playerTurnTimeSec = ((EditText) findViewById(R.id.main_editTextPlayerTimeSec)).getText().toString();
        String cardBuyTimeMin = ((EditText) findViewById(R.id.main_editTextCardBuyTimeMin)).getText().toString();
        String cardBuyTimeSec = ((EditText) findViewById(R.id.main_editTextCardBuyTimeSec)).getText().toString();

        long playerTurnTime = 0;
        if (!playerTurnTimeMin.isEmpty() || !playerTurnTimeSec.isEmpty()) {
            if (!playerTurnTimeMin.isEmpty()) {
                playerTurnTime += Long.parseLong(playerTurnTimeMin) * 60000;
            }
            if (!playerTurnTimeSec.isEmpty()) {
                playerTurnTime += Long.parseLong(playerTurnTimeSec) * 1000;
            }
        }
        if (playerTurnTime <= 0) {
            playerTurnTime = TMTimerApplicationUtil.DEFAULT_PLAYER_TURN_TIME_IN_MS;
        }

        long cardBuyTime = 0;
        if (!cardBuyTimeMin.isEmpty() || !cardBuyTimeSec.isEmpty()) {
            if (!cardBuyTimeMin.isEmpty()) {
                cardBuyTime += Long.parseLong(cardBuyTimeMin) * 60000;
            }
            if (!cardBuyTimeSec.isEmpty()) {
                cardBuyTime += Long.parseLong(cardBuyTimeSec) * 1000;
            }
        }
        if (cardBuyTime <= 0) {
            cardBuyTime = TMTimerApplicationUtil.DEFAULT_ALL_PLAYERS_CARD_BUY_TIME_IN_MS;
        }
        intent.putExtra(PLAYER_TURN_TIME_IN_MS, playerTurnTime);
        intent.putExtra(ALL_PLAYERS_CARD_BUY_TIME_IN_MS, cardBuyTime);

        startActivity(intent);
    }

    public void onSoundButton(View view) {
        TMTimerApplicationUtil.manageSoundButtonClick(view);
        TMTimerApplicationUtil.playSound(this.backgroundSound);
    }
}
