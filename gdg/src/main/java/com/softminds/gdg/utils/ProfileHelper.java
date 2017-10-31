
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
