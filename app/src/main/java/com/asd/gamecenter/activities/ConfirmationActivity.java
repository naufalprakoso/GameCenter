package com.asd.gamecenter.activities;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asd.gamecenter.R;
import com.asd.gamecenter.broadcast.SMSReceiver;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.database.GameCenterHelper;

@SuppressLint({"StaticFieldLeak", "SetTextI18n"})
public class ConfirmationActivity extends AppCompatActivity {

    private GameCenterHelper gameCenterHelper;

    private int balance;
    private String getId;

    private SmsManager smsManager;
    private PendingIntent pendingIntent;

    private TextView txtBalance, txtPrice, txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        txtBalance = findViewById(R.id.txt_balance);
        txtPrice = findViewById(R.id.txt_price);
        txtResult = findViewById(R.id.txt_result);

        getSupportActionBar().setTitle(R.string.payment_confirmation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnPay = findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Checkout().execute();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
        getId = sharedPreferences.getString(Key.USER_ID, null);

        gameCenterHelper = new GameCenterHelper(this);
        gameCenterHelper.open();

        smsManager = SmsManager.getDefault();
        Intent intent = new Intent(this, SMSReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new LoadTotalPrice().execute();
        new LoadWalletBalance().execute();
        new PreparePaymentConfirmation().execute();
    }

    private class Checkout extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return gameCenterHelper.checkoutCart(getId, balance);
        }

        @SuppressLint("UnlocalizedSms")
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            smsManager.sendTextMessage(
                    "554",
                    null,
                    "Congrats, your transaction has been successful - GameCenter",
                    pendingIntent,
                    null);

            Toast.makeText(ConfirmationActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class PreparePaymentConfirmation extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return gameCenterHelper.getPaymentConfirmation(getId);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            balance = result;
            txtResult.setText("Rp " + result);
        }
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

            txtPrice.setText("Rp " + totalPrice);
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

            txtBalance.setText("Rp " + balance);
        }
    }
}
