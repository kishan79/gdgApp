package com.softminds.gdg;

import android.app.Application;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.softminds.gdg.utils.AppUsers;


public class App extends Application {

    public AppUsers appUser = null;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(getApplicationContext(),R.string.low_memory_warn,Toast.LENGTH_SHORT).show();
        // TODO: 29/10/17 We will do some optimization here later not today to cause less memory uses
    }
}
