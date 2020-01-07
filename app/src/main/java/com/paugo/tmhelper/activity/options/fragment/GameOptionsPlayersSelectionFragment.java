package com.paugo.tmhelper.activity.options.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paugo.tmhelper.R;
import com.paugo.tmhelper.activity.game.PlayerTimerActivity;

public class GameOptionsPlayersSelectionFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static GameOptionsPlayersSelectionFragment newInstance(int page, String title) {
        GameOptionsPlayersSelectionFragment gameOptionsPlayersSelectionFragment = new GameOptionsPlayersSelectionFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        gameOptionsPlayersSelectionFragment.setArguments(args);
        return gameOptionsPlayersSelectionFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_options_selection_players, container, false);

        return view;
    }

    public void startGame(View view) {
        view.setEnabled(false);

        Intent intent = new Intent(getActivity(), PlayerTimerActivity.class);


        startActivity(intent);
    }
}