package com.shariqansari.watchme.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shariqansari.watchme.R;

import spencerstudios.com.bungeelib.Bungee;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    //    Firebase fields...
    private FirebaseAuth firebaseAuth;

    //    Android fields...
    private Button buttonCreateAccount, buttonLogIn;
    private KenBurnsView kenBurnsViewWelcome;
    private TextView textViewWelcome, textViewJourney, textViewTerms;

    //    Fonts fields...
    private Typeface typefaceBold, typefaceRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//      Firebase fields initialization...
        firebaseAuth = FirebaseAuth.getInstance();

//      Android fields initialization...
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewJourney = findViewById(R.id.textViewStartJourneyWelcome);
        textViewTerms = findViewById(R.id.textViewTermsWelcome);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccountWelcome);
        buttonLogIn = findViewById(R.id.buttonLogInWelcome);
        kenBurnsViewWelcome = findViewById(R.id.kenBurnViewBackgroundWelcome);

//      Font fields initialization...
        typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/quicksand_bold.otf");
        typefaceRegular = Typeface.createFromAsset(getAssets(), "fonts/quicksand_regular.otf");

//      Setting fonts...
        textViewWelcome.setTypeface(typefaceBold);
        textViewJourney.setTypeface(typefaceRegular);
        textViewTerms.setTypeface(typefaceRegular);
        buttonCreateAccount.setTypeface(typefaceBold);
        buttonLogIn.setTypeface(typefaceBold);

//      Event listeners...
        buttonCreateAccount.setOnClickListener(this);
        buttonLogIn.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserLoggedIn();
    }

    public void checkUserLoggedIn() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCreateAccountWelcome:
                startActivity(new Intent(WelcomeActivity.this, NameActivity.class));
                Bungee.slideLeft(this);
                break;
            case R.id.buttonLogInWelcome:
                startActivity(new Intent(WelcomeActivity.this, LogInActivity.class));
                Bungee.slideLeft(this);
                break;
        }
    }
}
