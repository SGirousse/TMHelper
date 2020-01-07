package com.paugo.tmhelper.activity.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paugo.tmhelper.R;
import com.paugo.tmhelper.activity.options.GameOptionsSelectionActivity;
import com.paugo.tmhelper.application.TMTimerApplicationUtil;
import com.paugo.tmhelper.pojo.Player;
import com.paugo.tmhelper.timer.TMTimerListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerTimerActivity extends AppCompatActivity implements TMTimerListener {

    public static final String ALL_PLAYERS_CARD_BUY_TIME_IN_MS = "ARG_ALL_PLAYERS_CARD_BUY_TIME_IN_MS";
    public static final String CURRENT_GENERATION_NUMBER = "CURRENT_GENERATION_NUMBER";
    private static final String TAG = "PlayerTimerActivity";
    private static final long BUTTON_DISABLE_TIME_MS = 3000;

    private int generation;
    private long cardBuyInMs;

    private ArrayList<Player> players;
    private int firstPlayerInCurrentGen;
    private int currentPlayer;
    private boolean isTurnGoingOn;

    private MediaPlayer playerAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_timer);

        Intent intent = getIntent();

        this.cardBuyInMs = intent.getLongExtra(GameOptionsSelectionActivity.ARG_ALL_PLAYERS_CARD_BUY_TIME_IN_MS, TMTimerApplicationUtil.DEFAULT_ALL_PLAYERS_CARD_BUY_TIME_IN_MS);
        Log.i(TAG, "Time allocated for card selection per generation (in ms) : " + this.cardBuyInMs);

        playersInitalization(intent);

        this.generation = 0;
        this.firstPlayerInCurrentGen = (int) (Math.random() * players.size());
        Log.i(TAG, "Random first player = " + this.firstPlayerInCurrentGen);

        this.playerAlert = TMTimerApplicationUtil.initializeSound(PlayerTimerActivity.this, R.raw.sd_scifi_alarm_nuclear);

        this.isTurnGoingOn = false;
        startGeneration();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.isTurnGoingOn) {
            startGeneration();
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

    private void playersInitalization(Intent intent) {
        long playerTimeInMs = intent.getLongExtra(GameOptionsSelectionActivity.ARG_PLAYER_TURN_TIME_IN_MS, 600000);

        Set<Integer> playersToDisable = new HashSet<>(4);
        playersToDisable.add(R.id.p2ConstraintLayout);
        playersToDisable.add(R.id.p3ConstraintLayout);
        playersToDisable.add(R.id.p4ConstraintLayout);
        playersToDisable.add(R.id.p5ConstraintLayout);

        this.players = new ArrayList<>();

        if (playerInitalization(intent, GameOptionsSelectionActivity.ARG_PLAYER_1_NAME, R.id.no1, R.id.currPlayerName1, R.id.playerTimeValue1, playerTimeInMs)) {
            if (playerInitalization(intent, GameOptionsSelectionActivity.ARG_PLAYER_2_NAME, R.id.no2, R.id.currPlayerName2, R.id.playerTimeValue2, playerTimeInMs)) {
                playersToDisable.remove(R.id.p2ConstraintLayout);
                if (playerInitalization(intent, GameOptionsSelectionActivity.ARG_PLAYER_3_NAME, R.id.no3, R.id.currPlayerName3, R.id.playerTimeValue3, playerTimeInMs)) {
                    playersToDisable.remove(R.id.p3ConstraintLayout);
                    if (playerInitalization(intent, GameOptionsSelectionActivity.ARG_PLAYER_4_NAME, R.id.no4, R.id.currPlayerName4, R.id.playerTimeValue4, playerTimeInMs)) {
                        playersToDisable.remove(R.id.p4ConstraintLayout);
                        if (playerInitalization(intent, GameOptionsSelectionActivity.ARG_PLAYER_5_NAME, R.id.no5, R.id.currPlayerName5, R.id.playerTimeValue5, playerTimeInMs)) {
                            playersToDisable.remove(R.id.p5ConstraintLayout);
                        }
                    }
                }
            }

            for (Integer i : playersToDisable) {
                findViewById(i).setVisibility(View.GONE);
            }

        } else {
            Log.e(TAG, "Player 1 is missing in intent : the game cannot starts !");
        }

        Log.i(TAG, "Number of players : " + players.size());
        Log.i(TAG, "Time allocated per player per generation (in ms) : " + playerTimeInMs);
    }

    private boolean playerInitalization(Intent intent, String IntentPlayerId, int textViewPlayerPlacementId, int textViewPlayerNameId, int textViewPlayerChronoId, long playerTimeInMs) {
        String playerName = intent.getStringExtra(IntentPlayerId);
        Log.d(TAG, "Initialize player " + playerName);
        if (playerName != null && !playerName.isEmpty()) {
            ((TextView) findViewById(textViewPlayerNameId)).setText(playerName);

            Player player = new Player(playerName, (TextView) findViewById(textViewPlayerChronoId), (TextView) findViewById(textViewPlayerPlacementId), playerTimeInMs, this);
            this.players.add(player);

            return true;
        }

        return false;
    }

    /**
     * Start a terraformation generation by selecting the first player, resetting players timers and starting turn of the new first player.
     */
    public void startGeneration() {
        this.isTurnGoingOn = true;
        this.firstPlayerInCurrentGen = getNextPlayerId(this.firstPlayerInCurrentGen);
        this.generation++;

        ((TextView) findViewById(R.id.textViewGenTitle)).setText("Génération " + this.generation);

        // reset clocks
        for (int p = 0; p < players.size(); p++) {
            Log.d(TAG, "Player managed : " + p + " (current first player = " + this.firstPlayerInCurrentGen + " )");
            players.get(p).resetPlayerForNewGeneration();
            players.get(p).getPlayerOrder().setText("" + getPlayerPlacement(p, this.firstPlayerInCurrentGen));
            setActivePlayer(false, p);
        }

        // select first player and start its turn !
        this.currentPlayer = this.firstPlayerInCurrentGen;
        setActivePlayer(true, this.currentPlayer);
        this.players.get(this.currentPlayer).startTurn();
    }

    @Override
    public void onTimeout() {
        playerNextGeneration();
    }

    @Override
    public void onCriticalLimit() {
        TMTimerApplicationUtil.playSound(this.playerAlert);
    }

    /**
     * When player action "Go to next generation" is triggered.
     *
     * @param view Source view
     */
    public void onPlayerNextGeneration(View view) {
        view.setEnabled(false);
        setButtonDisabledForDelay((Button) view);

        playerNextGeneration();
    }

    /**
     * Actions triggered when a player wants to go to next generation.
     */
    public void playerNextGeneration() {
        this.players.get(this.currentPlayer).goToNextGeneration();
        playerNextPlayer();
    }

    /**
     * When player action "Go to next player" is triggered.
     *
     * @param view Source view
     */
    public void onPlayerNextPlayer(View view) {
        view.setEnabled(false);
        setButtonDisabledForDelay((Button) view);

        playerNextPlayer();
    }

    public void playerNextPlayer() {
        if (this.playerAlert != null) {
            this.playerAlert.pause();
        } else {
            Log.w(TAG, "The player alert sound has not been initialized");
        }

        this.players.get(this.currentPlayer).goToNextPlayer();
        setActivePlayer(false, this.currentPlayer);

        // The loop can go back to player initially saying "Next player".
        int i = 0;
        do {
            i++;
            this.currentPlayer = getNextPlayerId(this.currentPlayer);
        } while (!this.players.get(this.currentPlayer).isActive() && i < players.size());

        if (this.players.get(this.currentPlayer).isActive()) {
            setActivePlayer(true, this.currentPlayer);
            this.players.get(this.currentPlayer).startTurn();
        } else {
            Log.i(TAG, "Fin de la génération no " + this.generation);
            interGenerationActivity();
        }
    }

    private void setActivePlayer(boolean isActive, int playerId) {
        switch (playerId) {
            case 0:
                setActivePlayerFashion(isActive, R.id.p1ConstraintLayout);
                break;
            case 1:
                setActivePlayerFashion(isActive, R.id.p2ConstraintLayout);
                break;
            case 2:
                setActivePlayerFashion(isActive, R.id.p3ConstraintLayout);
                break;
            case 3:
                setActivePlayerFashion(isActive, R.id.p4ConstraintLayout);
                break;
            case 4:
                setActivePlayerFashion(isActive, R.id.p5ConstraintLayout);
                break;
            default:
                Log.e(TAG, "Player id " + playerId + " is not managed yet.");
        }
    }

    private void setActivePlayerFashion(boolean isActive, int textViewPlayerPlacementId) {
        float alpha = isActive ? 1.0f : 0.5f;

        ConstraintLayout playerLayout = findViewById(textViewPlayerPlacementId);
        for (int i = 0; i < playerLayout.getChildCount(); i++) {
            playerLayout.getChildAt(i).setAlpha(alpha);
        }
    }

    private void interGenerationActivity() {
        this.isTurnGoingOn = false;

        Intent intent = new Intent(this, InterGenActivity.class);

        intent.putExtra(ALL_PLAYERS_CARD_BUY_TIME_IN_MS, this.cardBuyInMs);
        intent.putExtra(CURRENT_GENERATION_NUMBER, this.generation);

        startActivityForResult(intent, -1);
    }

    private int getNextPlayerId(int currentVal) {
        return ++currentVal > (this.players.size() - 1) ? 0 : currentVal;
    }

    private int getPlayerPlacement(int playerListId, int firstPlayerListId) {
        int playerPlacement = 1 + playerListId - firstPlayerListId;
        return playerListId >= firstPlayerListId ? playerPlacement : (playerPlacement + players.size());
    }

    public void onSoundButton(View view) {
        TMTimerApplicationUtil.manageSoundButtonClick(view);
        TMTimerApplicationUtil.playSound(this.playerAlert);
    }

    public void onEndGameButton(View view) {
        // TODO Start end game activity
    }

    public void onPause(View view) {
        this.isTurnGoingOn = !this.isTurnGoingOn;
        if (this.isTurnGoingOn) {
            view.setBackgroundColor(getResources().getColor(R.color.tm_dark_2, null));
        } else {
            view.setBackgroundColor(Color.RED);
        }
        findViewById(R.id.playerTimer_nextPlayerButton).setEnabled(this.isTurnGoingOn);
        findViewById(R.id.playerTimer_nextGenButton).setEnabled(this.isTurnGoingOn);
        this.players.get(this.currentPlayer).setTimerPaused(!this.isTurnGoingOn);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Souhaitez-vous revenir au menu principal ?\nCela annulera la partie en cours.")
                .setTitle("Retour menu principal")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PlayerTimerActivity.this.finish();
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

    private void setButtonDisabledForDelay(final Button button) {
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setEnabled(PlayerTimerActivity.this.isTurnGoingOn);
                    }
                });
            }
        }, BUTTON_DISABLE_TIME_MS);
    }
}
