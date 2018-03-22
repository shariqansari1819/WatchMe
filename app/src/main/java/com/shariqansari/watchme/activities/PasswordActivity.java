package com.shariqansari.watchme.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.extras.Constants;
import com.shariqansari.watchme.extras.L;
import com.shariqansari.watchme.pojo.User;

import dmax.dialog.SpotsDialog;
import spencerstudios.com.bungeelib.Bungee;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    //    Android fields...
    private TextView textViewPassword, textViewDes;
    private EditText editTextPassword, editTextConfirmPassword;
    private Button buttonContinue;
    private Toolbar toolbarPassword;
    private android.app.AlertDialog alertDialog;

    //    Fonts fields...
    private Typeface typefaceBold, typefaceRegular;

    //    Firebase fields...
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //    Instance variables...
    private String name, email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        //        Android fields initialization...
        textViewPassword = findViewById(R.id.textViewPasswordPassword);
        textViewDes = findViewById(R.id.textViewPasswordDescriptionPassword);
        editTextPassword = findViewById(R.id.editTextPasswordPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPasswordPassword);
        buttonContinue = findViewById(R.id.buttonCreateAccountRegister);
        toolbarPassword = findViewById(R.id.appBarPassword);
        alertDialog = new SpotsDialog(this, R.style.Custom);

        //        Fonts fields initialization...
        typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/quicksand_bold.otf");
        typefaceRegular = Typeface.createFromAsset(getAssets(), "fonts/quicksand_regular.otf");

//        Firebase fields initialization...
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        name = getIntent().getStringExtra(Constants.NAME);
        email = getIntent().getStringExtra(Constants.EMAIL);

//        Setting action bar...
        setSupportActionBar(toolbarPassword);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        Setting fonts...
        textViewPassword.setTypeface(typefaceBold);
        textViewDes.setTypeface(typefaceRegular);
        editTextPassword.setTypeface(typefaceRegular);
        editTextConfirmPassword.setTypeface(typefaceRegular);
        buttonContinue.setTypeface(typefaceBold);

//        Setting event listeners...
        buttonContinue.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        createUser(password, confirmPassword);
    }

    private void createUser(final String password, String confirmPassword) {
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)) {
            if (password.equals(confirmPassword)) {
                alertDialog.show();
                alertDialog.setCancelable(false);
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userId = firebaseAuth.getCurrentUser().getUid();
                            User user = new User(userId, name, email, password, "none", "none", "none", "I am using watch me app.", email.substring(0, email.indexOf("@")));
                            firebaseFirestore.collection("Users").document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(PasswordActivity.this, HomeActivity.class));
                                        Bungee.slideLeft(PasswordActivity.this);
                                        finish();
                                    } else {
                                        if (task.getException() != null) {
                                            L.T(PasswordActivity.this, task.getException().getMessage());
                                            alertDialog.dismiss();
                                        }
                                    }
                                    alertDialog.dismiss();
                                }
                            });
                        } else {
                            if (task.getException() != null) {
                                L.T(PasswordActivity.this, task.getException().getMessage());
                                alertDialog.dismiss();
                            }
                        }
                    }
                });
            } else {
                L.T(this, "Password did not match.");
            }
        } else {
            L.T(this, "Please fill out all the fields.");
        }
    }
}