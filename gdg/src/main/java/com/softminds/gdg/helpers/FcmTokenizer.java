
/*
*   Copyright (c) Ashar Khan 2017. <ashar786khan@gmail.com>
*    This file is part of Google Developer Group's Android Application.
*   Google Developer Group 's Android Application is free software : you can redistribute it and/or modify
*    it under the terms of GNU General Public License as published by the Free Software Foundation,
*   either version 3 of the License, or (at your option) any later version.
*
*   This Application is distributed in the hope that it will be useful
*    but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*   or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General  Public License for more details.
*
*   You should have received a copy of the GNU General Public License along with this Source File.
*   If not, see <http:www.gnu.org/licenses/>.
 */


package com.softminds.gdg.helpers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FcmTokenizer extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
       String token =  FirebaseInstanceId.getInstance().getToken();
       //this is the fcm token to send the notifications to this user
        Log.d(this.getClass().getSimpleName(),"Updated FCM Token :"+token);
        if(token !=null){
            sendTokenToServer(token);
        }else{
            Log.d(this.getClass().getSimpleName(),"Ignored the new Token that was generated because " +
                    "user was not logged to send this new token to server");
        }
    }

    private void sendTokenToServer(@NonNull String token) {
        if(!token.isEmpty()) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            //noinspection ConstantConditions
            database.child("root").child("fcmTokens").child(FirebaseAuth.getInstance().getUid()).setValue(token)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Tokenizer :","Saved the token of this user to database");
                }
            });
        }
    }
}
