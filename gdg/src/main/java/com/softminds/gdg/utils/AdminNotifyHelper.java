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

package com.softminds.gdg.utils;



import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AdminNotifyHelper {

    private String title;
    private String body;
    private boolean silently;
    private long liveTime;
    private long time;
    private String author;


    public AdminNotifyHelper(){
        //required
    }

    public AdminNotifyHelper(String title, String body, boolean silentNotify, long expireTime){
        this.title = title;
        this.body = body;
        this.silently = silentNotify;
        this.liveTime = expireTime;
        init();
    }

    private void init() {
        this.time = System.currentTimeMillis();
        //noinspection ConstantConditions
        this.author = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public String getBody() {
        return body;
    }

    public long getTime() {
        return time;
    }

    public String getAuthor(){
        return this.author;
    }

    public long getLiveTime() {
        return liveTime;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSilently() {
        return silently;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setLiveTime(long liveTime) {
        this.liveTime = liveTime;
    }

    public void setSilently(boolean silently) {
        this.silently = silently;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String auth){
        this.author = auth;
    }

    public static void CheckUser(final AdminAccess adminAccess){
       final FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
       if(user!=null){
           FirebaseDatabase.getInstance().getReference()
                   .child("root")
                   .child("admins")
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           for (DataSnapshot child:dataSnapshot.getChildren()) {
                               if(Objects.equals(child.getValue(String.class), user.getEmail())) {
                                   adminAccess.onResult(true);
                                   return;
                               }
                               adminAccess.onResult(false);
                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {
                           //ignore
                       }
                   });
       }else {
           adminAccess.InvalidAuth();
       }
    }

    public interface AdminAccess{
        void onResult(boolean granted);
        void InvalidAuth();
    }

    public static Task<Void> NotifyAll(AdminNotifyHelper payload){
        return FirebaseDatabase.getInstance().getReference()
                .child("root")
                .child("notifications")
                .setValue(payload);
    }
}
