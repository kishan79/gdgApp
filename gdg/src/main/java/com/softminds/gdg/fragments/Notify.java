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
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.softminds.gdg.R;
import com.softminds.gdg.utils.AdminNotifyHelper;

import java.util.concurrent.TimeUnit;

public class Notify extends Fragment {


    TextInputEditText title,body;
    Switch aSwitch;
    Button button;

    public Notify() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notify, container, false);
        button = v.findViewById(R.id.send_notification);
        aSwitch = v.findViewById(R.id.switch_sound);
        body = v.findViewById(R.id.notification_body);
        title = v.findViewById(R.id.notification_title);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(validate()){
                   String Title = title.getText().toString();
                   String Body = body.getText().toString();
                   boolean silently = !aSwitch.isChecked();
                   //noinspection ConstantConditions
                   long livetime = 60 * 1000 * 60;

                   AdminNotifyHelper adminNotifyHelper = new AdminNotifyHelper(
                           Title,
                           Body,
                           silently,
                           livetime
                   );

                   AdminNotifyHelper.NotifyAll(adminNotifyHelper).addOnSuccessListener(
                           new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(getContext(),R.string.notified_soon,Toast.LENGTH_SHORT).show();
                               }
                           }
                   );

                   title.setText(null);
                   body.setText(null);
                   button.setEnabled(false);
               }
            }
        });
    }

    private boolean validate() {
        if(body.getText().toString().isEmpty()){
            body.setError(getString(R.string.missing));
            title.requestFocus();
            return false;
        }
        if(title.getText().toString().isEmpty()){
            title.setError(getString(R.string.missing));
            title.requestFocus();
            return false;
        }
        return true;
    }
}
