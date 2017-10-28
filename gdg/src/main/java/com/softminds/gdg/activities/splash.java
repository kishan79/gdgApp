package com.softminds.gdg.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.softminds.gdg.R;

public class splash extends AppCompatActivity {

    private static final long TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),FirebaseAuth.getInstance().
                        getCurrentUser()==null?
                        LoginActivity.class : MainActivity.class));
                finish();
            }
        },TIME_OUT);
    }
}
