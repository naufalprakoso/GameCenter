package com.asd.gamecenter.activities.auth;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.asd.gamecenter.R;
import com.asd.gamecenter.database.GameCenterHelper;
import com.asd.gamecenter.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtName, edtEmail, edtPassword,
            edtConfirmPassword, edtBirthday, edtPhone;
    private RadioButton radioMale, radioFemale;
    private Spinner spinnerStatus;

    private Calendar calendar;

    private GameCenterHelper gameCenterHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnRegister = findViewById(R.id.btn_register);
        ImageView imgBack = findViewById(R.id.img_back);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        edtBirthday = findViewById(R.id.edt_birthday);
        edtPhone = findViewById(R.id.edt_phone);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        spinnerStatus = findViewById(R.id.spinner_status);

        ArrayAdapter adapterType = ArrayAdapter.createFromResource(
                this,
                R.array.status_array,
                android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterType);

        calendar = Calendar.getInstance();

        gameCenterHelper = new GameCenterHelper(this);
        gameCenterHelper.open();

        btnRegister.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        edtBirthday.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_birthday:
                new DatePickerDialog(
                        this,
                        R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                updateDate();
                            }
                        },
                        1999,
                        1,
                        1
                ).show();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_register:
                final String name = edtName.getText().toString();
                final String email = edtEmail.getText().toString();
                final String password = edtPassword.getText().toString();
                final String confirmPassword = edtConfirmPassword.getText().toString();
                final String birthday = edtBirthday.getText().toString();
                final String phone = edtPhone.getText().toString();
                final String status = spinnerStatus.getSelectedItem().toString();
                String gender = "";

                if (radioMale.isChecked()){
                    gender = "Male";
                }else if(radioFemale.isChecked()){
                    gender = "Female";
                }

                if (name.isEmpty()){
                    edtName.setError(getString(R.string.must_be_filled));
                }else if (name.length() < 3 || name.length() > 25){
                    edtName.setError("Name must be 3 - 25 character(s)");
                }else if (email.isEmpty()){
                    edtEmail.setError(getString(R.string.must_be_filled));
                }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edtEmail.setError(getString(R.string.enter_valid_email));
                }else if (password.isEmpty()){
                    edtPassword.setError(getString(R.string.must_be_filled));
                }else if (password.length() < 6){
                    edtPassword.setError(getString(R.string.password_short));
                }else if (confirmPassword.isEmpty()){
                    edtConfirmPassword.setError(getString(R.string.must_be_filled));
                }else if (!confirmPassword.equals(password)){
                    edtConfirmPassword.setError(getString(R.string.password_must_be_the_same));
                }else if (birthday.isEmpty()){
                    edtBirthday.setError(getString(R.string.choose_birthday_error));
                }else if (phone.isEmpty()){
                    edtPhone.setError(getString(R.string.must_be_filled));
                }else if (phone.length() < 10 || phone.length() > 12){
                    edtPhone.setError("Phone number must be 10 - 12 number(s)");
                }else if (status.equals("Choose Status")){
                    Toast.makeText(this, R.string.choose_status, Toast.LENGTH_SHORT).show();
                }else if (gender.isEmpty()){
                    Toast.makeText(this, R.string.choose_gender, Toast.LENGTH_SHORT).show();
                }else{
                    Random random = new Random();
                    Integer rand1 = random.nextInt(10);
                    Integer rand2 = random.nextInt(10);
                    Integer rand3 = random.nextInt(10);
                    String id = "US" + rand1 + rand2 + rand3;

                    User user = new User();
                    user.setId(id);
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setBirthday(birthday);
                    user.setPhone(phone);
                    user.setStatus(status);
                    user.setGender(gender);
                    user.setBalance(0);

                    gameCenterHelper.insertUser(user);

                    Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void updateDate() {
        final String myFormat = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);

        edtBirthday.setText(simpleDateFormat.format(calendar.getTime()));
    }
}
