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

package com.softminds.gdg.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softminds.gdg.App;
import com.softminds.gdg.R;
import com.softminds.gdg.helpers.ShortEventAdapter;
import com.softminds.gdg.utils.AdminNotifyHelper;
import com.softminds.gdg.utils.GdgEvents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeSection extends Fragment {

    RecyclerView recyclerView;
    TextView notificationTitle, notificationMessage,notificationAuthorTime;
    TextView lastMessage,Events;
    CardView card;

    ShortEventAdapter adapter = new ShortEventAdapter();

    ProgressBar progressBar;

    List<GdgEvents> events = new ArrayList<>();

    public HomeSection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_section, container, false);
        card = v.findViewById(R.id.notification_card);
        Events = v.findViewById(R.id.home_upcoming_event);
        lastMessage = v.findViewById(R.id.home_last_message);
        recyclerView = v.findViewById(R.id.home_recycler_event);
        notificationMessage = v.findViewById(R.id.notification_message);
        notificationTitle = v.findViewById(R.id.notification_title);
        progressBar = v.findViewById(R.id.home_progress);


        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProgressShow(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        loadData();

    }

    private void loadData() {

        //noinspection ConstantConditions
        if(((App)getActivity().getApplication()).events != null && ((App)getActivity().getApplication()).message != null) {
            adapter = new ShortEventAdapter(events);
            recyclerView.setAdapter(adapter);
            loadCardData(((App)getActivity().getApplication()).message);
            ProgressShow(false);
        }else {

            FirebaseDatabase.getInstance().getReference()
                    .child("root")
                    .child("events")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    events.add(snapshot.getValue(GdgEvents.class));
                                }
                                adapter = new ShortEventAdapter(events);
                                recyclerView.setAdapter(adapter);
                                loadMessage();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void loadMessage() {
        FirebaseDatabase.getInstance().getReference()
                .child("root")
                .child("notifications")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            loadCardData(dataSnapshot.getValue(AdminNotifyHelper.class));
                            if(getActivity()!=null){
                                ((App)getActivity().getApplication()).message = dataSnapshot.getValue(AdminNotifyHelper.class);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadCardData(AdminNotifyHelper value) {
        notificationMessage.setText(value.getBody());
        notificationTitle.setText(value.getTitle());
        String text = getString(R.string.author) + " : " + value.getAuthor() + "\n"
                + getString(R.string.time) + " : "
                + SimpleDateFormat.getDateTimeInstance().format(new Date(value.getTime()));
        notificationAuthorTime.setText(text);
        ProgressShow(false);
    }

    void ProgressShow(boolean yes){
        progressBar.setVisibility(yes? View.VISIBLE : View.GONE);

        card.setVisibility(yes? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(yes? View.GONE : View.VISIBLE);
        lastMessage.setVisibility(yes? View.GONE : View.VISIBLE);
        Events.setVisibility(yes? View.GONE : View.VISIBLE);

    }

}
