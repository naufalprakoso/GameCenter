package com.asd.gamecenter.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asd.gamecenter.R;
import com.asd.gamecenter.data.LoadMyGame;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.Game;

import java.util.ArrayList;
import java.util.Random;

@SuppressLint({"SetTextI18n", "RecyclerView", "StaticFieldLeak"})
public class MyGameAdapter extends RecyclerView.Adapter<MyGameAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Game> games;
    private GameCenterHelper gameCenterHelper;
    private RecyclerView recyclerView;

    public MyGameAdapter(Context context, ArrayList<Game> games, GameCenterHelper gameCenterHelper, RecyclerView recyclerView) {
        this.context = context;
        this.games = games;
        this.gameCenterHelper = gameCenterHelper;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_game, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.txtTitle.setText(games.get(i).getName());
        viewHolder.txtDescription.setText(games.get(i).getDescription());
        viewHolder.txtGenre.setText("Genre: " + games.get(i).getGenre());
        viewHolder.txtPlayingHour.setText(games.get(i).getPlayingHour() + "");

        viewHolder.btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int resultRandom = random.nextInt(5) + 1;

                int resultPlaying = games.get(i).getPlayingHour() + resultRandom;

                gameCenterHelper.updatePlayingHours(games.get(i).getId(), resultPlaying);

                new LoadMyGame(gameCenterHelper, context, recyclerView).execute();

                Toast.makeText(context,
                        "You have playing " + games.get(i).getName() + " " + resultRandom + " hour(s)",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtGenre, txtDescription, txtPlayingHour;
        Button btnPlayGame;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnPlayGame = itemView.findViewById(R.id.btn_play_game);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtGenre = itemView.findViewById(R.id.txt_genre);
            txtDescription = itemView.findViewById(R.id.txt_description);
            txtPlayingHour = itemView.findViewById(R.id.txt_playing_hours);
        }
    }
}
