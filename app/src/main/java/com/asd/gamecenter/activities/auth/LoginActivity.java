package com.asd.gamecenter.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asd.gamecenter.R;
import com.asd.gamecenter.activities.HomeActivity;
import com.asd.gamecenter.data.Key;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private GameCenterHelper gameCenterHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btn_login);
        TextView txtRegister = findViewById(R.id.txt_register);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        gameCenterHelper = new GameCenterHelper(this);
        gameCenterHelper.open();

        btnLogin.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                final String email = edtEmail.getText().toString();
                final String password = edtPassword.getText().toString();

                if (email.isEmpty()){
                    edtEmail.setError(getString(R.string.must_be_filled));
                }else if (password.isEmpty()){
                    edtPassword.setError(getString(R.string.must_be_filled));
                }else{
                    User user = gameCenterHelper.auth(new User(
                            null,
                            null,
                            email,
                            password,
                            null
                    ));

                    if (user != null){
                        Toast.makeText(this, "Welcome, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.txt_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(Key.APP_NAME, Context.MODE_PRIVATE);
        String getEmail = sharedPreferences.getString(Key.USER_EMAIL, null);

        if (getEmail != null){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
