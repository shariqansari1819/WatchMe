package com.shariqansari.watchme.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.felix.bottomnavygation.BottomNav;
import com.felix.bottomnavygation.ItemNav;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shariqansari.watchme.R;
import com.shariqansari.watchme.fragments.HomeFragment;
import com.shariqansari.watchme.fragments.ProfileFragment;

import spencerstudios.com.bungeelib.Bungee;

public class HomeActivity extends AppCompatActivity implements BottomNav.OnTabSelectedListener {

    //    Firebase fields...
    private FirebaseAuth firebaseAuthHome;

    //    Android fields...
    private BottomNav bottomNav;
    private Toolbar toolbarHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //    Firebase fields initialization...
        firebaseAuthHome = FirebaseAuth.getInstance();

        //   Android fields initialization...
        bottomNav = findViewById(R.id.bottomNav);
        toolbarHome = findViewById(R.id.appBarHome);

        bottomNav.addItemNav(new ItemNav(this, R.drawable.tab_home).addColorAtive(R.color.colorBlack).addColorInative(R.color.colorPrimaryText));
        bottomNav.addItemNav(new ItemNav(this, R.drawable.tab_share).addColorAtive(R.color.colorGradientAccent).addColorInative(R.color.colorGradientAccent));
        bottomNav.addItemNav(new ItemNav(this, R.drawable.tab_profile).addColorAtive(R.color.colorBlack).addColorInative(R.color.colorPrimaryText));
        bottomNav.build();

        setSupportActionBar(toolbarHome);
        if (getSupportActionBar() != null) {
            setAppBarTitle("Home");
        }

        switchFragment(new HomeFragment());
        bottomNav.selectTab(0);

        bottomNav.setTabSelectedListener(this);

    }

    private void setAppBarTitle(String title) {
        toolbarHome.setTitle(title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserLoggedIn();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogOut:
                firebaseAuthHome.signOut();
                checkUserLoggedIn();
                return true;
            case R.id.menuSetting:
                startActivity(new Intent(HomeActivity.this, SetupActivity.class));
                return true;
        }
        return false;
    }

    public void checkUserLoggedIn() {
        FirebaseUser firebaseUser = firebaseAuthHome.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
            Bungee.slideRight(this);
            finish();
        }
    }

    @Override
    public void onTabSelected(int i) {
        switch (i) {
            case 0:
                switchFragment(new HomeFragment());
                setAppBarTitle("Home");
                break;
            case 1:
                switchFragment(new HomeFragment());
                setAppBarTitle("Home");
                startActivity(new Intent(HomeActivity.this, CameraActivity.class));
                Bungee.slideUp(this);
                break;
            case 2:
                switchFragment(new ProfileFragment());
                setAppBarTitle("Profile");
        }
    }

    @Override
    public void onTabLongSelected(int i) {

    }

    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerMain, fragment);
        fragmentTransaction.commit();
    }

}