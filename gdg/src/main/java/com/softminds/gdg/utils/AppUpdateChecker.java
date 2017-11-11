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



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softminds.gdg.BuildConfig;


public class AppUpdateChecker {

    public static void CheckUpdate(final UpdateListener listener){
        FirebaseDatabase.getInstance().getReference()
                .child("root")
                .child("updates")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null && dataSnapshot.exists() && dataSnapshot.hasChildren()){
                            String ver = "",change = "",link = "";
                            int vCode = -1;
                            boolean required = false;
                            for (DataSnapshot sn :dataSnapshot.getChildren()) {
                                switch (sn.getKey()){
                                    case "versionCode":
                                        //noinspection ConstantConditions
                                        vCode = sn.getValue(int.class);
                                        break;
                                    case "version":
                                        ver = sn.getValue(String.class);
                                        break;
                                    case "link":
                                        link = sn.getValue(String.class);
                                        break;
                                    case "changelog":
                                        change = sn.getValue(String.class);
                                        break;
                                    case "required":
                                        //noinspection ConstantConditions
                                        required = sn.getValue(boolean.class);
                                }
                            }
                            if(BuildConfig.VERSION_CODE < vCode){
                                //update is available
                                listener.OnUpdateAvailable(vCode,ver,change,required,link);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public interface UpdateListener{
        void OnUpdateAvailable(int versionCode,String versionName, String changeLogs, boolean mustUpdate,String url);
    }
}
