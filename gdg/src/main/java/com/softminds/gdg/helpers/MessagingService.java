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


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.softminds.gdg.R;
import com.softminds.gdg.activities.LoginActivity;
import com.softminds.gdg.activities.MainActivity;

import java.util.Objects;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        //We will not send any payload to this device so no need to check this.

        Log.d(getClass().getSimpleName(),"Message Received from server while in foreground");

        //fixme : Make this logic backend for better results

        FirebaseDatabase.getInstance().getReference()
                .child("root")
                .child("notifications")
                .child("author")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String email = dataSnapshot.getValue(String.class);
                        //noinspection ConstantConditions
                        if(Objects.equals(email, FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            Log.i("MessagingService","The sender of notification is the current user... Skip the next steps");
                            notifyMessageSent();
                        }
                        else
                            handleMessage(remoteMessage);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void handleMessage(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, FirebaseAuth.getInstance().getCurrentUser() == null ? LoginActivity.class:MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent1 = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        Uri defaultURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,NotificationCompat.CATEGORY_MESSAGE)
                .setSmallIcon(R.drawable.gdg_notification_icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultURI)
                .setContentIntent(intent1);

        NotificationManager manager =  ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        if (manager != null) {
            manager.notify(23,builder.build());
        }


    }

    private void notifyMessageSent(){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this,NotificationCompat.CATEGORY_STATUS)
                .setSmallIcon(R.drawable.gdg_notification_icon)
                .setContentTitle(getString(R.string.delivered_message))
                .setContentText(getString(R.string.delivered_long))
                .setSound(uri)
                .setAutoCancel(true);

        NotificationManager manager =  (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(24,builder2.build());
        }
    }

}
