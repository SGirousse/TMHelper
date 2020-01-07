package com.paugo.tmhelper.activity.options.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paugo.tmhelper.R;

public class GameOptionsTimingsSelectionFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static GameOptionsTimingsSelectionFragment newInstance(int page, String title) {
        GameOptionsTimingsSelectionFragment gameOptionsPlayersSelectionFragment = new GameOptionsTimingsSelectionFragment();
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
        View view = inflater.inflate(R.layout.fragment_game_options_selection_timings, container, false);
        //TextView tvLabel = (TextView) view.findViewById(R.id.gameOptionsSelectionTimings_textView);
        //tvLabel.setText(page + " -- " + title);
        return view;
    }
}