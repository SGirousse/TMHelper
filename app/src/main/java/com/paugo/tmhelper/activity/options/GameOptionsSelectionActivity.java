package com.paugo.tmhelper.activity.options;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.paugo.tmhelper.R;
import com.paugo.tmhelper.activity.game.PlayerTimerActivity;
import com.paugo.tmhelper.activity.options.viewer.SectionsPagerAdapter;
import com.paugo.tmhelper.application.TMTimerApplicationUtil;

public class GameOptionsSelectionActivity extends AppCompatActivity {

    public static final String ARG_PLAYER_1_NAME = "ARG_PLAYER_1_NAME";
    public static final String ARG_PLAYER_2_NAME = "ARG_PLAYER_2_NAME";
    public static final String ARG_PLAYER_3_NAME = "ARG_PLAYER_3_NAME";
    public static final String ARG_PLAYER_4_NAME = "ARG_PLAYER_4_NAME";
    public static final String ARG_PLAYER_5_NAME = "ARG_PLAYER_5_NAME";
    public static final String ARG_PLAYER_TURN_TIME_IN_MS = "ARG_PLAYER_TURN_TIME_IN_MS";
    public static final String ARG_ALL_PLAYERS_CARD_BUY_TIME_IN_MS = "ARG_ALL_PLAYERS_CARD_BUY_TIME_IN_MS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options_selection);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, PlayerTimerActivity.class);

        String player1Name = ((EditText) findViewById(R.id.gameOptionsSelectionPlayers_editTextPlayerName1)).getText().toString();
        intent.putExtra(ARG_PLAYER_1_NAME, player1Name);
        String player2Name = ((EditText) findViewById(R.id.gameOptionsSelectionPlayers_editTextPlayerName2)).getText().toString();
        intent.putExtra(ARG_PLAYER_2_NAME, player2Name);
        String player3Name = ((EditText) findViewById(R.id.gameOptionsSelectionPlayers_editTextPlayerName3)).getText().toString();
        intent.putExtra(ARG_PLAYER_3_NAME, player3Name);
        String player4Name = ((EditText) findViewById(R.id.gameOptionsSelectionPlayers_editTextPlayerName4)).getText().toString();
        intent.putExtra(ARG_PLAYER_4_NAME, player4Name);
        String player5Name = ((EditText) findViewById(R.id.gameOptionsSelectionPlayers_editTextPlayerName5)).getText().toString();
        intent.putExtra(ARG_PLAYER_5_NAME, player5Name);

        String playerTurnTimeMin = ((EditText) findViewById(R.id.gameOptionsSelectionTimings_editTextPlayerTimeMin)).getText().toString();
        String playerTurnTimeSec = ((EditText) findViewById(R.id.gameOptionsSelectionTimings_editTextPlayerTimeSec)).getText().toString();
        String cardBuyTimeMin = ((EditText) findViewById(R.id.gameOptionsSelectionTimings_editTextCardBuyTimeMin)).getText().toString();
        String cardBuyTimeSec = ((EditText) findViewById(R.id.gameOptionsSelectionTimings_editTextCardBuyTimeSec)).getText().toString();

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
        intent.putExtra(ARG_PLAYER_TURN_TIME_IN_MS, playerTurnTime);
        intent.putExtra(ARG_ALL_PLAYERS_CARD_BUY_TIME_IN_MS, cardBuyTime);

        startActivity(intent);
    }
}