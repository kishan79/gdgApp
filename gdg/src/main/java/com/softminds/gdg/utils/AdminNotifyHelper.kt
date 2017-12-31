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


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminNotifyHelper {

    var title: String? = null
    var body: String? = null
    var isSilently: Boolean = false
    var liveTime: Long = 0
    var time: Long = 0
    var author: String? = null


    @Suppress("unused")
    constructor() {
        //required for fire base to assemble remote messages received via FCM
    }

    constructor(title: String, body: String, silentNotify: Boolean, expireTime: Long) {
        this.title = title
        this.body = body
        this.isSilently = silentNotify
        this.liveTime = expireTime
        init()
    }

    private fun init() {
        this.time = System.currentTimeMillis()

        this.author = FirebaseAuth.getInstance().currentUser!!.email
    }

    interface AdminAccess {
        fun onResult(granted: Boolean)
        fun invalidAuth()
    }

    companion object {

        fun checkUser(adminAccess: AdminAccess) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                FirebaseDatabase.getInstance().reference
                        .child("root")
                        .child("admins")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (child in dataSnapshot.children) {
                                    if (child.getValue(String::class.java) == user.email) {
                                        adminAccess.onResult(true)
                                        return
                                    }
                                    adminAccess.onResult(false)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                //ignore
                            }
                        })
            } else {
                adminAccess.invalidAuth()
            }
        }

        fun notifyAll(payload: AdminNotifyHelper): Task<Void> {
            return FirebaseDatabase.getInstance().reference
                    .child("root")
                    .child("notifications")
                    .setValue(payload)
        }
    }
}
