package com.asd.gamecenter.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.TextView;

import com.asd.gamecenter.database.GameCenterHelper;


@SuppressLint("StaticFieldLeak")
public class LoadTotalPrice extends AsyncTask<Void, Void, Integer> {

    private GameCenterHelper gameCenterHelper;
    private TextView txtPrice;

    public LoadTotalPrice(GameCenterHelper gameCenterHelper, TextView txtPrice) {
        this.gameCenterHelper = gameCenterHelper;
        this.txtPrice = txtPrice;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return gameCenterHelper.getTotalPriceCart();
    }

    @Override
    protected void onPostExecute(Integer totalPrice) {
        super.onPostExecute(totalPrice);

        txtPrice.setText(totalPrice.toString());
    }
}
