package com.asd.gamecenter.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.asd.gamecenter.R;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.data.LoadMyGame;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.User;

public class HomeFragment extends Fragment {

    private GameCenterHelper gameCenterHelper;
    private RecyclerView recyclerView;
    private TextView txtPhone, txtEmail, txtName, txtEmpty;

    private String getId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.rv_my_game);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameCenterHelper = new GameCenterHelper(getContext());
        gameCenterHelper.open();

        txtEmail = view.findViewById(R.id.txt_email);
        txtPhone = view.findViewById(R.id.txt_phone);
        txtName = view.findViewById(R.id.txt_name);
        txtEmpty = view.findViewById(R.id.txt_empty);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
        getId = sharedPreferences.getString(Key.USER_ID, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new CountMyGame().execute();
        new LoadUserProfile().execute();
    }

    private class LoadUserProfile extends AsyncTask<Void, Void, User> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(Void... voids) {
            return gameCenterHelper.getUserProfile(getId);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            txtEmail.setText(user.getEmail());
            txtPhone.setText(user.getPhone());
            txtName.setText(user.getName());
        }
    }

    private class CountMyGame extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return gameCenterHelper.countMyGame();
        }

        @Override
        protected void onPostExecute(Integer total) {
            super.onPostExecute(total);

            if(total < 1){
                txtEmpty.setVisibility(View.VISIBLE);
            }else{
                new LoadMyGame(gameCenterHelper, getContext(), recyclerView).execute();
            }
        }
    }
}