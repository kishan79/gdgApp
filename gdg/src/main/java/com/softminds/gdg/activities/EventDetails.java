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

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softminds.gdg.App;
import com.softminds.gdg.R;
import com.softminds.gdg.helpers.PicEventRecycler;
import com.softminds.gdg.utils.Constants;
import com.softminds.gdg.utils.GdgEvents;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventDetails extends AppCompatActivity {

    List<GdgEvents> events;

    GdgEvents source;

    //Resources References
    TextView speakers,about_event,agenda,time_venue;
    Typeface product_sans;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        product_sans = Typeface.createFromAsset(getAssets(), Constants.PathConstants.PRODUCT_SANS_FONT);

        //Grab all references
        speakers = findViewById(R.id.event_detail_speakers_list_text);
        about_event = findViewById(R.id.event_detail_about_event_text);
        agenda = findViewById(R.id.event_detail_agenda_list_text);
        time_venue = findViewById(R.id.event_detail_time_venue_text);
        recyclerView = findViewById(R.id.event_pics_recycler);
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
            source = events.get(getIntent().getIntExtra("POSITION",-1));
            loadData();
        }
    }

    private void loadData() {

        if(source.getPicsUrl() == null){
            findViewById(R.id.card_event_pics).setVisibility(View.GONE);
        }else {
            PicEventRecycler adapter = new PicEventRecycler(source.getPicsUrl());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }


        ActionBar bar;
        if (getSupportActionBar() != null) {
            bar = getSupportActionBar();
            bar.setTitle(source.getName());
        }
        Glide.with(this).load(source.getHeadIconUrl()).into(((ImageView) findViewById(R.id.event_detail_image)));

        speakers.setTypeface(product_sans);
        about_event.setTypeface(product_sans);
        agenda.setTypeface(product_sans);
        time_venue.setTypeface(product_sans);

        agenda.setText(source.getAgenda()==null ? format(null) : source.getAgenda());
        time_venue.setText(format(source.getVenue(), SimpleDateFormat.getDateInstance().format(new Date(source.getTime()))));
        about_event.setText(source.getExtra_details());
        speakers.setText(format(source.getSpeakers()));

    }

    @NonNull
    private String format(String venue, String format) {
        return venue + " " +  getString(R.string.on)+ " " + format;
    }

    @NonNull
    private String format(@Nullable List<String> speakers) {
        StringBuilder builder = new StringBuilder();
        if(speakers ==null || speakers.size() ==0){
            return "\n\n\n\n"+getString(R.string.coming_soon);
        }
        else{
            for(String each:speakers){
               builder.append(each).append("\n\n");
            }
        }
        return builder.toString();
    }

}
