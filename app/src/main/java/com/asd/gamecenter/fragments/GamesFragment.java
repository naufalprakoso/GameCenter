package com.asd.gamecenter.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asd.gamecenter.R;
import com.asd.gamecenter.adapters.GameAdapter;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.Game;

import java.util.ArrayList;

public class GamesFragment extends Fragment {

    private GameCenterHelper gameCenterHelper;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        recyclerView = view.findViewById(R.id.rv_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameCenterHelper = new GameCenterHelper(getContext());
        gameCenterHelper.open();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadGame().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class LoadGame extends AsyncTask<Void, Void, ArrayList<Game>> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Game> doInBackground(Void... voids) {
            return gameCenterHelper.viewGame();
        }

        @Override
        protected void onPostExecute(ArrayList<Game> gameList) {
            super.onPostExecute(gameList);

//            Log.d("InfoHome", gameList.get(0).getName());

            recyclerView.setAdapter(new GameAdapter(getContext(), gameList));
        }
    }
}