package com.asd.gamecenter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asd.gamecenter.R;
import com.asd.gamecenter.database.GameCenterHelper;

public class CartFragment extends Fragment {

    GameCenterHelper gameCenterHelper;

    RecyclerView recyclerView;
    TextView txtPrice, txtEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        txtPrice = view.findViewById(R.id.txt_price);
        txtEmpty = view.findViewById(R.id.txt_empty);
        recyclerView = view.findViewById(R.id.rv_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameCenterHelper = new GameCenterHelper(getContext());
        gameCenterHelper.open();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadCart(gameCenterHelper, recyclerView, txtEmpty, txtPrice, getContext()).execute();
        new LoadTotalPrice(gameCenterHelper, txtPrice).execute();
    }
}
