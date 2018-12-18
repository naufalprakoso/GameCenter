package com.asd.gamecenter.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asd.gamecenter.R;
import com.asd.gamecenter.data.Key;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
        String getName = sharedPreferences.getString(Key.USER_NAME, null);

        TextView txtWelcome = view.findViewById(R.id.txt_welcome);


        txtWelcome.setText("Welcome, " + getName + "!");

        return view;
    }
}