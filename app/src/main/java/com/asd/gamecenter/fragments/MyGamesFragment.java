package com.asd.gamecenter.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asd.gamecenter.R;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.Game;

import java.util.ArrayList;

public class MyGamesFragment extends Fragment {

    private GameCenterHelper gameCenterHelper;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_games, container, false);

        recyclerView = view.findViewById(R.id.rv_my_game);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameCenterHelper = new GameCenterHelper(getContext());
        gameCenterHelper.open();

        return view;
    }

    public class LoadMyGames extends AsyncTask<Void, Void, ArrayList<Game>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Game> doInBackground(Void... voids) {
            return gameCenterHelper.viewMyGame();
        }

        @Override
        protected void onPostExecute(ArrayList<Game> myGameList) {
            super.onPostExecute(myGameList);

//            recyclerView.setAdapter(new ItemAdapter(getContext(), itemList));
        }
    }
}
