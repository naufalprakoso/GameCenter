package com.asd.gamecenter.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.asd.gamecenter.adapters.MyGameAdapter;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.Game;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class LoadMyGame extends AsyncTask<Void, Void, ArrayList<Game>> {

    private GameCenterHelper gameCenterHelper;
    private Context context;
    private RecyclerView recyclerView;

    public LoadMyGame(GameCenterHelper gameCenterHelper, Context context, RecyclerView recyclerView) {
        this.gameCenterHelper = gameCenterHelper;
        this.context = context;
        this.recyclerView = recyclerView;
    }

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

        recyclerView.setAdapter(new MyGameAdapter(context, myGameList, gameCenterHelper, recyclerView));
    }
}
