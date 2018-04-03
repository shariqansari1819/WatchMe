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
import com.shariqansari.watchme.fragments.UsersFragment;

import spencerstudios.com.bungeelib.Bungee;

public class HomeActivity extends AppCompatActivity implements BottomNav.OnTabSelectedListener {

    //    Firebase fields...
    private FirebaseAuth firebaseAuthHome;

    //    Android fields...
    private BottomNav bottomNav;
    private Toolbar toolbarHome;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private UsersFragment usersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //    Firebase fields initialization...
        firebaseAuthHome = FirebaseAuth.getInstance();

        //   Android fields initialization...
        bottomNav = findViewById(R.id.bottomNav);
        toolbarHome = findViewById(R.id.appBarHome);
        homeFragment = HomeFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
        usersFragment = UsersFragment.newInstance();

        bottomNav.addItemNav(new ItemNav(this, R.drawable.tab_home).addColorAtive(R.color.colorBlack).addColorInative(R.color.colorPrimaryText));
        bottomNav.addItemNav(new ItemNav(this, R.drawable.tab_share).addColorAtive(R.color.colorGradientAccent).addColorInative(R.color.colorGradientAccent));
        bottomNav.addItemNav(new ItemNav(this, R.drawable.tab_profile).addColorAtive(R.color.colorBlack).addColorInative(R.color.colorPrimaryText));
        bottomNav.addItemNav(new ItemNav(this, R.drawable.tab_share).addColorAtive(R.color.colorBlack).addColorInative(R.color.colorPrimaryText));
        bottomNav.build();

        setSupportActionBar(toolbarHome);
        if (getSupportActionBar() != null) {
            setAppBarTitle("Home");
        }

        initializeFragments();
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
                switchFragment(homeFragment);
                setAppBarTitle("Home");
                break;
            case 1:
                switchFragment(homeFragment);
                setAppBarTitle("Home");
                startActivity(new Intent(HomeActivity.this, CameraActivity.class));
                bottomNav.selectTab(0);
                Bungee.slideUp(this);
                break;
            case 2:
                switchFragment(profileFragment);
                setAppBarTitle("Profile");
            case 3:
                switchFragment(usersFragment);
        }
    }

    @Override
    public void onTabLongSelected(int i) {

    }

    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment == fragment) {
            fragmentTransaction.hide(profileFragment);
            fragmentTransaction.hide(usersFragment);
        } else if (profileFragment == fragment) {
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(usersFragment);
        } else if (usersFragment == fragment) {
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(profileFragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    public void initializeFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainerMain, homeFragment, "home");
        fragmentTransaction.add(R.id.fragmentContainerMain, profileFragment, "profile");
        fragmentTransaction.add(R.id.fragmentContainerMain, usersFragment, "users");
        fragmentTransaction.hide(profileFragment);
        fragmentTransaction.hide(usersFragment);
        fragmentTransaction.commit();
    }

}