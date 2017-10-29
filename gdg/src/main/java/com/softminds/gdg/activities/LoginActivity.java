package com.softminds.gdg.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
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
        else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.sure_to_leave)
                    .setMessage(R.string.back_message)
                    .setCancelable(true)
                    .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                                FirebaseAuth.getInstance().signOut();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.login_fragment_container, new WelcomeFragment()).commit();
                        }
                    }).show();
        }
    }
}
