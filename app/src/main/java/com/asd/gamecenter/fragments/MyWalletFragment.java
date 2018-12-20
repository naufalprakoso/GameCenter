package com.asd.gamecenter.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asd.gamecenter.R;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.database.GameCenterHelper;

public class MyWalletFragment extends Fragment {

    private GameCenterHelper gameCenterHelper;

    private TextView txtBalance;
    private Button btnTopUp;
    private EditText edtAmount;

    private String getId;
    private Integer getBalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_wallet, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
        getId = sharedPreferences.getString(Key.USER_ID, null);

        txtBalance = view.findViewById(R.id.txt_balance);
        btnTopUp = view.findViewById(R.id.btn_top_up);
        edtAmount = view.findViewById(R.id.edt_amount);

        gameCenterHelper = new GameCenterHelper(getContext());
        gameCenterHelper.open();

        btnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer amount = Integer.parseInt(edtAmount.getText().toString());

                if(amount > 0){
                    getBalance += amount;

                    gameCenterHelper.updateBalance(getBalance, getId);
                    edtAmount.setText("");

                    Toast.makeText(getContext(), "Top up successful", Toast.LENGTH_SHORT).show();
                    new LoadWalletBalance().execute();
                }else{
                    Toast.makeText(getContext(), "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadWalletBalance().execute();
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

            txtBalance.setText("Rp " + balance);
            getBalance = balance;
        }
    }

}
