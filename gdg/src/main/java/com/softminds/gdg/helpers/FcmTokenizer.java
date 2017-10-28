package com.softminds.gdg.helpers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.softminds.gdg.utils.AppUsers;
import com.softminds.gdg.utils.Constants;


public class FcmTokenizer extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
       String token =  FirebaseInstanceId.getInstance().getToken();
       //this is the fcm token to send the notifications to this user
        Log.d(this.getClass().getSimpleName(),"Updated FCM Token :"+token);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            sendTokenToServer(token);
        }else{
            Log.d(this.getClass().getSimpleName(),"Ignored the new Token that was generated because " +
                    "user was not logged to send this new token to server");
        }
    }

    private void sendTokenToServer(String token) {
        String userToken = AppUsers.generateAppUserToken();
        if (userToken == null || userToken.isEmpty()){
            //this should not happen ever
            Log.w(this.getClass().getSimpleName(),"Critical Error : Unique token for user node is null");
            return;
        }
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.DatabaseConstants.ROOT)
                .child(Constants.DatabaseConstants.USERS_LIST)
                .child(userToken)
                .child("fcmToken")
                .setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FcmTokenizer","Fcm token Update Successful");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("FcmTokenizer","Failed to set the token to server : This user will not receive notifications ",e);
            }
        });
    }
}
