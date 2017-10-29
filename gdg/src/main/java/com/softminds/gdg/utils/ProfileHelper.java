package com.softminds.gdg.utils;


import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
This class will not be initiated directly. Instead it will be used
as a helper in the Profile Database Operations.
 */
public class ProfileHelper {


    public static Task<Void> initiate(AppUsers appUsers){
        //noinspection ConstantConditions
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.DatabaseConstants.ROOT)
                .child(Constants.DatabaseConstants.USERS_LIST)
                .child(AppUsers.generateAppUserToken());
        reference.setValue(appUsers);
        //noinspection ConstantConditions
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
               .child(Constants.DatabaseConstants.ROOT)
               .child(Constants.DatabaseConstants.USERS_LINK)
               .child(AppUsers.generateAppUserToken());
       return ref.setValue(reference.toString());
    }


    public static void check(final CheckListener listener) throws IllegalStateException{
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
         throw new IllegalStateException("Cannot check for user unless user is not signed In");
        }
        else {
            //noinspection ConstantConditions
            FirebaseDatabase.getInstance().getReference()
                    .child(Constants.DatabaseConstants.ROOT)
                    .child(Constants.DatabaseConstants.USERS_LINK)
                    .child(AppUsers.generateAppUserToken())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                                listener.OnProfileExist(dataSnapshot.getValue(String.class));
                            else
                                listener.OnNotExist();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.wtf("ProfileHelper.Check","Something very Unexpected just happened, " +
                                    "it was not supposed to happen. It means the Permission" +
                                    " was denied to check the user list. Reason :" + databaseError.getMessage());
                        }
                    });
        }
    }

    public interface CheckListener{
        void OnProfileExist(String profileUrl);
        void OnNotExist();
    }
}
