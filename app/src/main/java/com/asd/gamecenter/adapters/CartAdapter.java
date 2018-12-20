package com.asd.gamecenter.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asd.gamecenter.R;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.data.LoadCart;
import com.asd.gamecenter.data.LoadTotalPrice;
import com.asd.gamecenter.model.Game;

import java.util.ArrayList;

@SuppressLint({"SetTextI18n", "RecyclerView"})
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Game> games;
    private GameCenterHelper gameCenterHelper;

    private TextView txtPrice, txtEmpty;
    private RecyclerView recyclerView;

    public CartAdapter(Context context, ArrayList<Game> games, GameCenterHelper gameCenterHelper, TextView txtPrice, TextView txtEmpty, RecyclerView recyclerView) {
        this.context = context;
        this.games = games;
        this.gameCenterHelper = gameCenterHelper;
        this.txtPrice = txtPrice;
        this.txtEmpty = txtEmpty;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.txtTitle.setText(games.get(i).getName());
        viewHolder.txtPrice.setText("Rp " + games.get(i).getPrice());
        viewHolder.txtQty.setText(games.get(i).getQty() + "");

        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameCenterHelper.deleteGame(games.get(i).getId());

                new LoadCart(gameCenterHelper, recyclerView, txtEmpty, txtPrice, context).execute();
                new LoadTotalPrice(gameCenterHelper, txtPrice).execute();

                Toast.makeText(context, "Games deleted from cart", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.btnMinQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQty = games.get(i).getQty() - 1;
                if (currentQty < 1){
                    Toast.makeText(context, "Game Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                }else{
                    gameCenterHelper.updateCartQty(currentQty, games.get(i).getId());

                    new LoadCart(gameCenterHelper, recyclerView, txtEmpty, txtPrice, context).execute();
                    new LoadTotalPrice(gameCenterHelper, txtPrice).execute();
                }
            }
        });

        viewHolder.btnPlusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQty = games.get(i).getQty() + 1;
                gameCenterHelper.updateCartQty(currentQty, games.get(i).getId());

                new LoadCart(gameCenterHelper, recyclerView, txtEmpty, txtPrice, context).execute();
                new LoadTotalPrice(gameCenterHelper, txtPrice).execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPrice, txtTitle, txtQty;
        ImageView imgDelete;
        Button btnPlusQty, btnMinQty;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPrice = itemView.findViewById(R.id.txt_price);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtQty = itemView.findViewById(R.id.txt_qty);
            imgDelete = itemView.findViewById(R.id.img_delete);
            btnPlusQty = itemView.findViewById(R.id.btn_plus_qty);
            btnMinQty = itemView.findViewById(R.id.btn_min_qty);
        }
    }
}
