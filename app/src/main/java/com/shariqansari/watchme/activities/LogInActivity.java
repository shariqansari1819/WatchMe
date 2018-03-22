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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.extras.L;

import dmax.dialog.SpotsDialog;
import spencerstudios.com.bungeelib.Bungee;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    //    Android fields...
    private Toolbar toolbarLogIn;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogIn;
    private TextView textViewForgot, textViewForgotClick, textViewNeedAccount;
    private android.app.AlertDialog alertDialog;
    private RelativeLayout relativeLayoutLogIn;

    //    Firebase fields...
    private FirebaseAuth firebaseAuth;

    //    Fonts fields...
    private Typeface typefaceBold;
    private Typeface typefaceRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        //  Android fields initialization...
        toolbarLogIn = findViewById(R.id.toolbarLogIn);
        editTextEmail = findViewById(R.id.editTextEmailLogIn);
        editTextPassword = findViewById(R.id.editTextPasswordLogIn);
        buttonLogIn = findViewById(R.id.buttonLogIn);
        textViewForgot = findViewById(R.id.textViewForgotPasswordLogin);
        textViewForgotClick = findViewById(R.id.textViewClickForgotPasswordLogIn);
        textViewNeedAccount = findViewById(R.id.textViewNeedNewAccountLogin);
        relativeLayoutLogIn = findViewById(R.id.relativeLayoutParentLogIn);
        alertDialog = new SpotsDialog(this, R.style.Custom);


        //  Fonts fields initialization...
        typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/quicksand_bold.otf");
        typefaceRegular = Typeface.createFromAsset(getAssets(), "fonts/quicksand_regular.otf");


        //  Firebase fields initialization...
        firebaseAuth = FirebaseAuth.getInstance();

        //  Setting action bar...
        setSupportActionBar(toolbarLogIn);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.log_in);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //  Setting fonts...
        editTextEmail.setTypeface(typefaceRegular);
        editTextPassword.setTypeface(typefaceRegular);
        buttonLogIn.setTypeface(typefaceBold);
        textViewForgot.setTypeface(typefaceRegular);
        textViewForgotClick.setTypeface(typefaceBold);
        textViewNeedAccount.setTypeface(typefaceBold);

        //  Event listeners...
        textViewNeedAccount.setOnClickListener(this);
        textViewForgotClick.setOnClickListener(this);
        buttonLogIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewNeedNewAccountLogin:
                startActivity(new Intent(LogInActivity.this, NameActivity.class));
                Bungee.slideRight(this);
                finish();
                break;
            case R.id.textViewClickForgotPasswordLogIn:
                break;
            case R.id.buttonLogIn:
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                loginUser(email, password);
                break;
        }
    }

    public void loginUser(String email, String password) {
        if (validateEmail(email) && validatePassword(password)) {
            alertDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LogInActivity.this, HomeActivity.class));
                        Bungee.slideLeft(LogInActivity.this);
                        finish();
                    } else {
                        if (task.getException() != null) {
                            L.S(relativeLayoutLogIn, task.getException().getMessage());
                        }
                    }
                    alertDialog.dismiss();
                }
            });
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
                L.S(relativeLayoutLogIn, "Invalid email address.");
                validation = false;
            }

        } else {
            L.S(relativeLayoutLogIn, "Email field cannot be empty.");
            validation = false;
        }

        return validation;
    }

    public boolean validatePassword(String password) {
        boolean validation;
        if (!TextUtils.isEmpty(password)) {
            validation = true;
        } else {
            validation = false;
            L.S(relativeLayoutLogIn, "Password field cannot be empty.");
        }
        return validation;
    }
}
