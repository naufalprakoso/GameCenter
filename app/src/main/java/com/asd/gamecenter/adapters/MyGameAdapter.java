package com.asd.gamecenter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asd.gamecenter.R;
import com.asd.gamecenter.model.Game;

import java.util.ArrayList;

public class MyGameAdapter extends RecyclerView.Adapter<MyGameAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Game> games;

    public MyGameAdapter(Context context, ArrayList<Game> games) {
        this.context = context;
        this.games = games;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_game, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

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
