package com.asd.gamecenter.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.asd.gamecenter.adapters.CartAdapter;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.Game;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class LoadCart extends AsyncTask<Void, Void, ArrayList<Game>> {

    private GameCenterHelper gameCenterHelper;
    private RecyclerView recyclerView;
    private TextView txtEmpty;
    private TextView txtPrice;
    private Context context;

    public LoadCart(GameCenterHelper gameCenterHelper, RecyclerView recyclerView, TextView txtEmpty, TextView txtPrice, Context context) {
        this.gameCenterHelper = gameCenterHelper;
        this.recyclerView = recyclerView;
        this.txtEmpty = txtEmpty;
        this.txtPrice = txtPrice;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Game> doInBackground(Void... voids) {
        return gameCenterHelper.viewCart();
    }

    @Override
    protected void onPostExecute(ArrayList<Game> gameList) {
        super.onPostExecute(gameList);

        if(gameList.size() < 1){
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

           recyclerView.setAdapter(new CartAdapter(context, gameList, gameCenterHelper, txtPrice, txtEmpty, recyclerView));
        }
    }
}
