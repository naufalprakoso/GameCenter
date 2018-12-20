package com.asd.gamecenter.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.asd.gamecenter.R;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.User;

@SuppressLint("StaticFieldLeak")
public class ProfileActivity extends AppCompatActivity {

    private GameCenterHelper gameCenterHelper;
    private String getId;

    private TextView txtPhone, txtEmail, txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtEmail = findViewById(R.id.txt_email);
        txtPhone = findViewById(R.id.txt_phone);
        txtName = findViewById(R.id.txt_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gameCenterHelper = new GameCenterHelper(this);
        gameCenterHelper.open();

        SharedPreferences sharedPreferences = getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
        getId = sharedPreferences.getString(Key.USER_ID, null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}
