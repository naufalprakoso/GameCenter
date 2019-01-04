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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asd.gamecenter.R;
import com.asd.gamecenter.broadcast.SMSReceiver;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.Game;

@SuppressLint({"StaticFieldLeak", "SetTextI18n"})
public class ConfirmationActivity extends AppCompatActivity {

    private GameCenterHelper gameCenterHelper;

    private SmsManager smsManager;
    private PendingIntent pendingIntent;

    private Game game;
    private EditText edtMoney;

    private Integer moneyConvert;
    private String getUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        SharedPreferences sharedPreferences = getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
        getUserId = sharedPreferences.getString(Key.USER_ID, null);
        game = getIntent().getParcelableExtra(Key.GAME_DETAIL);

        TextView txtPrice = findViewById(R.id.txt_price);
        edtMoney = findViewById(R.id.edt_money);

        getSupportActionBar().setTitle(R.string.payment_confirmation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnPay = findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = edtMoney.getText().toString();

                if(money.isEmpty()){
                    edtMoney.setError(getString(R.string.must_be_filled));
                }else{
                    moneyConvert = Integer.parseInt(money);

                    if (moneyConvert < 1){
                        Toast.makeText(ConfirmationActivity.this, "Your money must be greater than 0", Toast.LENGTH_SHORT).show();
                    }else if(moneyConvert < game.getPrice()){
                        Toast.makeText(ConfirmationActivity.this, "Your money is not sufficient for this transaction", Toast.LENGTH_SHORT).show();
                    }else{
                        new Checkout().execute();
                    }
                }
            }
        });

        txtPrice.setText("Rp " + game.getPrice());

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

    private class Checkout extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return gameCenterHelper.checkoutCart(game, getUserId);
        }

        @SuppressLint("UnlocalizedSms")
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            smsManager.sendTextMessage(
                    "554",
                    null,
                    "Congrats, your transaction has been successful. Your transaction change is " + (moneyConvert - game.getPrice()),
                    pendingIntent,
                    null);

            Toast.makeText(ConfirmationActivity.this, "Payment Successful, please check your SMS", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
