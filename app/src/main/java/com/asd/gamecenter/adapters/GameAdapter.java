package com.asd.gamecenter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.asd.gamecenter.R;
import com.asd.gamecenter.activities.GameDetailActivity;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.model.Game;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Game> games;

    public GameAdapter(Context context, ArrayList<Game> games) {
        this.context = context;
        this.games = games;
    }

    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.txtTitle.setText(games.get(i).getName());
        viewHolder.txtStock.setText(games.get(i).getStock() + " pc(s) left");
        viewHolder.txtPrice.setText("Rp " + games.get(i).getPrice());
        viewHolder.txtGenre.setText("Genre: " + games.get(i).getGenre());
        viewHolder.ratingBar.setRating((float) games.get(i).getRating());

        if (games.get(i).getDescription().length() > 250){
            viewHolder.txtDescription.setText(games.get(i).getDescription().substring(0, 250) + "...");
        }else{
            viewHolder.txtDescription.setText(games.get(i).getDescription());
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameDetailActivity.class);
                intent.putExtra(Key.GAME_DETAIL, games.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        RatingBar ratingBar;
        TextView txtTitle, txtPrice, txtStock,
                txtGenre, txtDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cv_game);
            ratingBar = itemView.findViewById(R.id.star_rating);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtStock = itemView.findViewById(R.id.txt_stock);
            txtGenre = itemView.findViewById(R.id.txt_genre);
            txtDescription = itemView.findViewById(R.id.txt_description);
        }
    }
}
