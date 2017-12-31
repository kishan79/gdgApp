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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softminds.gdg.App;
import com.softminds.gdg.R;
import com.softminds.gdg.activities.EventDetails;
import com.softminds.gdg.helpers.LongEventAdapter;
import com.softminds.gdg.helpers.RecyclerItemClick;
import com.softminds.gdg.helpers.ShortEventAdapter;
import com.softminds.gdg.utils.GdgEvents;
import com.softminds.gdg.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class EventLists extends Fragment implements RecyclerItemClickListener {

    RecyclerView mRecycler;
    ProgressBar progressBar;

    public EventLists() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_lists, container, false);
        // Inflate the layout for this fragment

        mRecycler = v.findViewById(R.id.main_recycler_event);
        progressBar = v.findViewById(R.id.long_adapter_loading);
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRecycler();
    }

    @SuppressWarnings("ConstantConditions")
    private void setRecycler() {
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecycler.addOnItemTouchListener(new RecyclerItemClick(getContext(),this));
        if(((App)getActivity().getApplication()).events !=null)
            mRecycler.setAdapter(new LongEventAdapter(((App)getActivity().getApplication()).events));
        else{
            ProgressLoad(true);
            final List<GdgEvents> events = new ArrayList<>();
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
                                mRecycler.setAdapter(new LongEventAdapter(events));
                                ProgressLoad(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void ProgressLoad(boolean b) {
        mRecycler.setVisibility(b ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(b ? View.VISIBLE  : View.GONE);
    }

    @Override
    public void onItemClick(int position, View data) {
        Intent i = new Intent(getContext(), EventDetails.class);
        i.putExtra("POSITION",position);
        startActivity(i);
    }
}
