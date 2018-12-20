package com.asd.gamecenter.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asd.gamecenter.R;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.database.GameCenterHelper;

@SuppressLint({"SetTextI18n", "StaticFieldLeak"})
public class HomeFragment extends Fragment {

    private GameCenterHelper gameCenterHelper;
    private String getId;

    private TextView txtMoney, txtCartCount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        gameCenterHelper = new GameCenterHelper(getContext());
        gameCenterHelper.open();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
        getId = sharedPreferences.getString(Key.USER_ID, null);
        String getName = sharedPreferences.getString(Key.USER_NAME, null);

        txtMoney = view.findViewById(R.id.txt_money);
        TextView txtWelcome = view.findViewById(R.id.txt_welcome);
        txtCartCount = view.findViewById(R.id.txt_cart_count);

        txtWelcome.setText("Welcome, " + getName + "!");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadWalletBalance().execute();
        new LoadCartCount().execute();
    }

    private class LoadWalletBalance extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return gameCenterHelper.getWalletBalance(getId);
        }

        @Override
        protected void onPostExecute(Integer balance) {
            super.onPostExecute(balance);

            txtMoney.setText("Rp " + balance);
        }
    }

    private class LoadCartCount extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return gameCenterHelper.cartCount();
        }

        @Override
        protected void onPostExecute(Integer cartCount) {
            super.onPostExecute(cartCount);

            txtCartCount.setText(cartCount.toString());
        }
    }
}