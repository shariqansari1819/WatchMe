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

import com.shariqansari.watchme.R;
import com.shariqansari.watchme.extras.Constants;
import com.shariqansari.watchme.extras.L;

import spencerstudios.com.bungeelib.Bungee;

public class NameActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    //    Android fields...
    private TextView textViewName, textViewDes;
    private EditText editTextName;
    private Button buttonContinue;
    private Toolbar toolbarName;

    //    Fonts fields...
    private Typeface typefaceBold, typefaceRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        //   Android fields initialization...
        textViewName = findViewById(R.id.textViewNameName);
        textViewDes = findViewById(R.id.textViewNameDescriptionName);
        editTextName = findViewById(R.id.editTextNameName);
        buttonContinue = findViewById(R.id.buttonContinueName);
        toolbarName = findViewById(R.id.appBarName);

        //   Fonts fields initialization...
        typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/quicksand_bold.otf");
        typefaceRegular = Typeface.createFromAsset(getAssets(), "fonts/quicksand_regular.otf");

        //   Setting action bar...
        setSupportActionBar(toolbarName);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //   Setting fonts...
        textViewName.setTypeface(typefaceBold);
        textViewDes.setTypeface(typefaceRegular);
        editTextName.setTypeface(typefaceRegular);
        buttonContinue.setTypeface(typefaceBold);

        buttonContinue.setEnabled(false);

        //   Setting event listeners...
        buttonContinue.setOnClickListener(this);
        editTextName.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonContinueName:
                sendToEmail(editTextName.getText().toString());
                break;
        }
    }

    private void sendToEmail(String name) {
        if (!TextUtils.isEmpty(name)) {
            Intent intent = new Intent(NameActivity.this, EmailActivity.class);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
            Bungee.slideLeft(this);
        } else {
            L.T(this, "Please fill out your name.");
        }
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
