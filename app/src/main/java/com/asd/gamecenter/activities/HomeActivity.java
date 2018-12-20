package com.asd.gamecenter.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.fragments.MyGamesFragment;
import com.asd.gamecenter.fragments.MyWalletFragment;
import com.asd.gamecenter.R;
import com.asd.gamecenter.activities.auth.LoginActivity;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.fragments.CartFragment;
import com.asd.gamecenter.fragments.GamesFragment;
import com.asd.gamecenter.fragments.HomeFragment;
import com.asd.gamecenter.fragments.InformationFragment;

@SuppressLint({"ApplySharedPref", "StaticFieldLeak"})
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private int sendSMSPermission;

    private FloatingActionButton fab;
    private GameCenterHelper gameCenterHelper;
    private int getTotalPrice, getBalance;
    private String getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
        getId = sharedPreferences.getString(Key.USER_ID, null);
        String getName = sharedPreferences.getString(Key.USER_NAME, null);
        String getEmail = sharedPreferences.getString(Key.USER_EMAIL, null);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.hide();

        gameCenterHelper = new GameCenterHelper(this);

        sendSMSPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
        );

        if(sendSMSPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.SEND_SMS
                    },1
            );
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        HomeFragment homeFragment = new HomeFragment();
        openFragment(homeFragment);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView txtHeaderName = headerView.findViewById(R.id.txt_name_header);
        TextView txtHeaderEmail = headerView.findViewById(R.id.txt_email_header);

        txtHeaderName.setText(getName);
        txtHeaderEmail.setText(getEmail);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            openFragment(homeFragment);

            fab.hide();
        } else if (id == R.id.nav_cart) {
            CartFragment cartFragment = new CartFragment();
            openFragment(cartFragment);

            gameCenterHelper.open();
            new LoadTotalPrice().execute();
            new LoadWalletBalance().execute();

            fab.show();
        } else if (id == R.id.nav_games) {
            GamesFragment gamesFragment = new GamesFragment();
            openFragment(gamesFragment);

            fab.hide();
        } else if (id == R.id.nav_wallet) {
            MyWalletFragment myWalletFragment = new MyWalletFragment();
            openFragment(myWalletFragment);

            fab.hide();
        } else if (id == R.id.nav_my_games) {
            MyGamesFragment myGamesFragment = new MyGamesFragment();
            openFragment(myGamesFragment);

            fab.hide();
        } else if (id == R.id.nav_information) {
            InformationFragment informationFragment = new InformationFragment();
            openFragment(informationFragment);

            fab.hide();
        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if (getTotalPrice > 0){
                    if(getBalance < getTotalPrice){
                        Toast.makeText(this, "Your balance is not sufficient for this transaction", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(this, ConfirmationActivity.class);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(this, "There is no game in your cart", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class LoadTotalPrice extends AsyncTask<Void, Void, Integer> {

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

            getTotalPrice = totalPrice;
        }
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

            getBalance = balance;
        }
    }
}
