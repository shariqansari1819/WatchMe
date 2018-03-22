package com.shariqansari.watchme.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.extras.Constants;
import com.shariqansari.watchme.extras.L;

import spencerstudios.com.bungeelib.Bungee;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    //    Android fields...
    private TextView textViewEmail, textViewDes;
    private EditText editTextEmail;
    private Button buttonContinue;
    private Toolbar toolbarEmail;

    //    Fonts fields...
    private Typeface typefaceBold, typefaceRegular;

    //    Instance variables...
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        //   Android fields initialization...
        textViewEmail = findViewById(R.id.textViewEmailEmail);
        textViewDes = findViewById(R.id.textViewEmailDescriptionEmail);
        editTextEmail = findViewById(R.id.editTextEmailEmail);
        buttonContinue = findViewById(R.id.buttonContinueEmail);
        toolbarEmail = findViewById(R.id.appBarEmail);

        name = getIntent().getStringExtra(Constants.NAME);

        //   Fonts fields initialization...
        typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/quicksand_bold.otf");
        typefaceRegular = Typeface.createFromAsset(getAssets(), "fonts/quicksand_regular.otf");

        //   Setting action bar...
        setSupportActionBar(toolbarEmail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        buttonContinue.setEnabled(false);

        //   Setting fonts...
        textViewEmail.setTypeface(typefaceBold);
        textViewDes.setTypeface(typefaceRegular);
        editTextEmail.setTypeface(typefaceRegular);
        buttonContinue.setTypeface(typefaceBold);

        //   Setting event listeners...
        buttonContinue.setOnClickListener(this);
        editTextEmail.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonContinueEmail:
                sendEmail(editTextEmail.getText().toString());
                break;
        }
    }

    public boolean validateEmail(String email) {
        boolean validation;

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!TextUtils.isEmpty(email)) {
            validation = true;
            if (email.matches(emailPattern)) {
                validation = true;
            } else {
                L.T(this, "Invalid email address.");
                validation = false;
            }

        } else {
            L.T(this, "Email field cannot be empty.");
            validation = false;
        }

        return validation;
    }

    private void sendEmail(String email) {
        if (validateEmail(email) && !TextUtils.isEmpty(name)) {
            Intent intent = new Intent(EmailActivity.this, PasswordActivity.class);
            intent.putExtra(Constants.NAME, name);
            intent.putExtra(Constants.EMAIL, email);
            startActivity(intent);
            Bungee.slideLeft(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.slideRight(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().isEmpty()) {
            buttonContinue.setEnabled(true);
        } else {
            buttonContinue.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
