package com.evilgeniusesapps.firebaseauth.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.evilgeniusesapps.firebaseauth.R;
import com.evilgeniusesapps.firebaseauth.fragments.LoginFragment;
import com.evilgeniusesapps.firebaseauth.interfaces.ChangeFragment;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener, ChangeFragment {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.stringAuthorization);
        setSupportActionBar(toolbar);
        setFragment(new LoginFragment());
    }

    @Override
    public boolean onSupportNavigateUp() {
        toolbar.setTitle(R.string.stringAuthorization);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setSupportActionBar(toolbar);
        setFragment(new LoginFragment());
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}