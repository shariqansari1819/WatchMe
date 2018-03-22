package com.shariqansari.watchme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shariqansari.watchme.R;

public class HomeActivity extends AppCompatActivity {

    //    Firebase fields...
    private FirebaseAuth firebaseAuthHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //    Firebase fields initialization...
        firebaseAuthHome = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserLoggedIn();
    }

    public void checkUserLoggedIn() {
        FirebaseUser firebaseUser = firebaseAuthHome.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
            finish();
        }
    }
}
