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

package com.softminds.gdg.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.softminds.gdg.App;
import com.softminds.gdg.R;
import com.softminds.gdg.utils.GdgEvents;

import java.util.List;

public class EventDetails extends AppCompatActivity {

    List<GdgEvents> events;

    GdgEvents source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        events = ((App)getApplication()).events;
        if(events == null || getIntent().getIntExtra("POSITION",-1) == -1){
            Log.wtf(getClass().getSimpleName(),"Impossible to click a view unless data is not loaded Or");
            Log.wtf(getClass().getSimpleName(),"Parent Activity/ Fragment did not provided the POSITION");
        }
        else {
            source = events.get(getIntent().getIntExtra("POSITION",0));
            loadData();
        }
    }

    private void loadData() {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(source.getName());
    }
}
