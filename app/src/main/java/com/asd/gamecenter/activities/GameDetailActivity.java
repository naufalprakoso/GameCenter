package com.asd.gamecenter.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asd.gamecenter.R;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.Game;

public class GameDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;

    private GameCenterHelper gameCenterHelper;
    private Game game;

    private String getId;
    private Boolean resultState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        game = getIntent().getExtras().getParcelable(Key.GAME_DETAIL);

        TextView txtTitle = findViewById(R.id.txt_title);
        TextView txtDescription = findViewById(R.id.txt_description);
        TextView txtPrice = findViewById(R.id.txt_price);
        TextView txtStock = findViewById(R.id.txt_stock);
        TextView txtGenre = findViewById(R.id.txt_genre);
        fab = findViewById(R.id.fab);
        RatingBar ratingBar = findViewById(R.id.star_rating);

        getSupportActionBar().setTitle(game.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getId = game.getId();

        txtTitle.setText(game.getName());
        txtDescription.setText(game.getDescription());
        txtPrice.setText("Rp " + game.getPrice());
        txtStock.setText(game.getStock() + " pc(s) left");
        txtGenre.setText("Genre: " + game.getGenre());
        ratingBar.setRating((float) game.getRating());

        fab.setOnClickListener(this);

        gameCenterHelper = new GameCenterHelper(this);
        gameCenterHelper.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadCartState().execute();
        new LoadMyGameState().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if(resultState){
                    Toast.makeText(this, "You already have this game in the cart", Toast.LENGTH_SHORT).show();
                }else{
                    gameCenterHelper.insertGame(game);

                    Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                new LoadCartState().execute();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }

    private class LoadCartState extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return gameCenterHelper.checkCartState(getId);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            resultState = result;
        }
    }

    private class LoadMyGameState extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return gameCenterHelper.checkMyGamesState(getId);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result){
                fab.hide();
            }
        }
    }
}
