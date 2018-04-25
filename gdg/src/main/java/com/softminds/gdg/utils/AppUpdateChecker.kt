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

package com.softminds.gdg.utils


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.softminds.gdg.BuildConfig


object AppUpdateChecker {

    fun checkUpdate(listener: UpdateListener) {
        FirebaseDatabase.getInstance().reference
                .child("root")
                .child("updates")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                            var ver: String? = ""
                            var change: String? = ""
                            var link: String? = ""
                            var vCode = -1
                            var required = false
                            for (sn in dataSnapshot.children) {
                                when (sn.key) {
                                    "versionCode" ->
                                        vCode = sn.getValue(Int::class.javaPrimitiveType)!!
                                    "version" -> ver = sn.getValue(String::class.java)
                                    "link" -> link = sn.getValue(String::class.java)
                                    "changelog" -> change = sn.getValue(String::class.java)
                                    "required" ->
                                        required = sn.getValue(Boolean::class.javaPrimitiveType)!!
                                }
                            }
                            if (BuildConfig.VERSION_CODE < vCode) {
                                //update is available
                                listener.onUpdateFound(vCode, ver, change, required, link)
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
    }

    interface UpdateListener {
        fun onUpdateFound(versionCode: Int, versionName: String?, changeLogs: String?, mustUpdate: Boolean, url: String?)
    }
}
