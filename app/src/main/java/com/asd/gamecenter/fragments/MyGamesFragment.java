package com.asd.gamecenter.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asd.gamecenter.R;
import com.asd.gamecenter.data.LoadMyGame;
import com.asd.gamecenter.database.GameCenterHelper;

public class MyGamesFragment extends Fragment {

    private GameCenterHelper gameCenterHelper;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_games, container, false);

        recyclerView = view.findViewById(R.id.rv_my_game);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameCenterHelper = new GameCenterHelper(getContext());
        gameCenterHelper.open();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadMyGame(gameCenterHelper, getContext(), recyclerView).execute();
    }
}
