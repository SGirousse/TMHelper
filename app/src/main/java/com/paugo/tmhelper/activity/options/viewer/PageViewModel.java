package com.paugo.tmhelper.activity.options.viewer;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.paugo.tmhelper.R;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<Integer> mLayoutId = Transformations.map(mIndex, new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer input) {
            return Integer.valueOf(R.layout.fragment_game_options_selection_players);
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<Integer> getLayout() {
        return mLayoutId;
    }
}