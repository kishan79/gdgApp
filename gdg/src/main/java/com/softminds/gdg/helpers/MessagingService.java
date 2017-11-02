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
import com.softminds.gdg.activities.MainActivity;

import java.util.Objects;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        //We will not send any payload to this device so no need to check this.

        if(FirebaseAuth.getInstance().getCurrentUser() ==null)
            return;
        FirebaseDatabase.getInstance().getReference()
                .child("root")
                .child("notifications")
                .child("email")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String email = dataSnapshot.getValue(String.class);
                        //noinspection ConstantConditions
                        if(Objects.equals(email, FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            Log.i("MessagingService","The sender of notification is the current user... Skip the next steps");
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent1 = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        String channel_id = getString(R.string.default_notification_channel_id);

        Uri defaultURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channel_id)
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

}
