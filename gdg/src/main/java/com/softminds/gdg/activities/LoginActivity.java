package com.softminds.gdg.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.softminds.gdg.R;
import com.softminds.gdg.fragments.WelcomeFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_fragment_container,new WelcomeFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        android.support.v4.app.Fragment attached = getSupportFragmentManager().findFragmentById(R.id.login_fragment_container);
        if(attached instanceof WelcomeFragment)
            super.onBackPressed();
        else
            getSupportFragmentManager().beginTransaction()
            .replace(R.id.login_fragment_container,new WelcomeFragment()).commit();
    }
}
