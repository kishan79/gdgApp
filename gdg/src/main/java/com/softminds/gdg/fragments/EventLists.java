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

import com.softminds.gdg.App;
import com.softminds.gdg.R;
import com.softminds.gdg.activities.EventDetails;
import com.softminds.gdg.helpers.LongEventAdapter;
import com.softminds.gdg.helpers.RecyclerItemClick;
import com.softminds.gdg.utils.RecyclerItemClickListener;


public class EventLists extends Fragment implements RecyclerItemClickListener {

    RecyclerView mRecycler;

    public EventLists() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_lists, container, false);
        // Inflate the layout for this fragment

        mRecycler = v.findViewById(R.id.main_recycler_event);
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
        mRecycler.setAdapter(new LongEventAdapter(((App)getActivity().getApplication()).events));
    }

    @Override
    public void OnItemClick(int position, View data) {
        Intent i = new Intent(getContext(), EventDetails.class);
        i.putExtra("POSITION",position);
        startActivity(i);
    }
}
