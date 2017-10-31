
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


package com.softminds.gdg;

import android.app.Application;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.softminds.gdg.utils.AppUsers;


public class App extends Application {

    public AppUsers appUser = null;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(getApplicationContext(),R.string.low_memory_warn,Toast.LENGTH_SHORT).show();
        // TODO: 29/10/17 We will do some optimization here later not today to cause less memory uses
    }
}
