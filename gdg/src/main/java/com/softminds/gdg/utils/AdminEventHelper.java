package com.softminds.gdg.utils;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class AdminEventHelper {

    public static Task<Void> AddEvent(GdgEvents e){
        return FirebaseDatabase.getInstance().getReference()
                .child("root")
                .child("events")
                .child(String.valueOf(System.currentTimeMillis()))
                .setValue(e);
    }
}
