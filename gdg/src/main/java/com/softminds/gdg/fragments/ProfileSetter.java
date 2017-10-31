
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


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.softminds.gdg.R;
import com.softminds.gdg.utils.AppUsers;
import com.softminds.gdg.utils.Constants;


public class ProfileSetter extends Fragment {


    View root;
    ActionBar actionBar;
    Typeface ProductSans;
    Button next,back;
    ConstraintLayout step0;
    LinearLayout step1,step2,step3;

    AlphaAnimation fade,appear;
    private long TIME_OUT = 1200;

    private int currentStep = 0;

    AppUsers newUser = AppUsers.defaultUser();

    public ProfileSetter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile_setter, container, false);
        next = root.findViewById(R.id.profile_edit_proceed_next);
        back = root.findViewById(R.id.profile_edit_proceed_prev);

        step0 = root.findViewById(R.id.step_zero_profile_edit);


        step1 = root.findViewById(R.id.step_first_profile_edit);
        step2 = root.findViewById(R.id.step_second_profile_edit);
        step3 = root.findViewById(R.id.step_third_profile_edit);

        fade = new AlphaAnimation(1.0f,0.0f);
        fade.setDuration(TIME_OUT);

        appear = new AlphaAnimation(0.0f,1.0f);
        appear.setDuration(TIME_OUT);

        back.setEnabled(false);

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //noinspection ConstantConditions
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.profile_complete);
        }
        ProductSans = Typeface.createFromAsset(getActivity().getAssets(), Constants.PathConstants.PRODUCT_SANS_FONT);
        //noinspection ConstantConditions
        String str = getString(R.string.hey) + " " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        ((TextView) root.findViewById(R.id.profile_edit_message_salutation)).setText(str);
        root.findViewById(R.id.profile_edit_proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root.findViewById(R.id.profile_edit_proceed_next).setVisibility(View.VISIBLE);
                root.findViewById(R.id.profile_edit_proceed_prev).setVisibility(View.VISIBLE);
                fadeView(step0);
                appearView(step1);
                currentStep = 1;
                if (actionBar != null) {
                    actionBar.setTitle(R.string.profile_complete_step1);
                }
            }
        });
        setTypeface();
        setNextButtonListener();
        setBackButtonListener();

    }

    private void setNextButtonListener() {

        root.findViewById(R.id.profile_edit_proceed_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentStep){
                    case 1 :
                        if(validate(1)) {
                            fadeView(step1);
                            appearView(step2);
                            currentStep = 2;
                            back.setEnabled(true);
                            if (actionBar != null) {
                                actionBar.setTitle(R.string.profile_complete_step2);
                            }
                        }
                        break;
                    case 2 :
                        if(validate(2)) {
                            fadeView(step2);
                            appearView(step3);
                            currentStep = 3;
                            back.setEnabled(true);
                            if (actionBar != null) {
                                actionBar.setTitle(R.string.profile_complete_step3);
                            }
                        }
                        break;
                    case 3 :
                        if(validate(3)) {
                            fadeView(step3);
                            StartAndShowUploadingProfileStatus();
                            //startActivity(new Intent(getContext(), MainActivity.class));
                        }
                        break;
                }
            }
        });
    }

    private void StartAndShowUploadingProfileStatus() {
        //newUser.setName();
    }

    private void setBackButtonListener() {
        switch (currentStep){
            //this button starts from second step
            case 3:
                fadeView(step3);
                appearView(step2);
                if (actionBar != null) {
                    actionBar.setTitle(R.string.profile_complete_step2);
                }
                currentStep = 2;
                break;
            case 2:
                fadeView(step2);
                appearView(step1);
                if (actionBar != null) {
                    actionBar.setTitle(R.string.profile_complete_step1);
                }
                currentStep = 1;
                break;

            }
    }

    private boolean validate(int i) {
        //Validate the step values as denoted by the 'i'
        // returns false if any error is detected in views
        switch (i){
            case 1:
                break;
            case 2:
                AppCompatCheckBox android,web,speaking,promotion,firebase,cloud,tensorflow;
                android = root.findViewById(R.id.profile_edit_skills_android);
                web = root.findViewById(R.id.profile_edit_skills_web);
                tensorflow = root.findViewById(R.id.profile_edit_skills_tensorFlow);
                speaking = root.findViewById(R.id.profile_edit_skills_speaking);
                promotion = root.findViewById(R.id.profile_edit_skills_promo);
                firebase = root.findViewById(R.id.profile_edit_skills_firebase);
                cloud = root.findViewById(R.id.profile_edit_skills_cloud);

                if(!android.isChecked()
                        && !web.isChecked()
                        && !speaking.isChecked()
                        && !promotion.isChecked()
                        && !firebase.isChecked()
                        && !cloud.isChecked()
                        && !tensorflow.isChecked()){
                    //if none of the skills clicked
                Toast.makeText(getContext(),R.string.skills_required,Toast.LENGTH_SHORT).show();
                return false;
                }
                return true;

            case 3:
               boolean ErrorStateChecked = !((AppCompatCheckBox) root.findViewById(R.id.profile_edit_step3_info)).isChecked();
               TextInputEditText AboutMeErrorState =  root.findViewById(R.id.profile_edit_about_me);
               if(AboutMeErrorState.getText().toString().isEmpty()){
                   AboutMeErrorState.setError(getString(R.string.required_field));
                   AboutMeErrorState.requestFocus();
                   return false;
               }
               if (ErrorStateChecked){
                   Toast.makeText(getContext(), R.string.checkbox_missing, Toast.LENGTH_SHORT).show();
                   return false;
               }
                return true;
        }
        return false;
    }

    private void fadeView(final View view){
        view.startAnimation(fade);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
            }
        }, TIME_OUT);
    }

    private void appearView(final View v){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.startAnimation(appear);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(View.VISIBLE);
                    }
                },TIME_OUT);
            }
        }, TIME_OUT);
    }

    private void setTypeface() {
        //set typeface to all the views in the layout
        ((TextView) root.findViewById(R.id.profile_edit_message)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_message_salutation)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_proceed)).setTypeface(ProductSans);
        ((TextInputEditText) root.findViewById(R.id.profile_edit_name)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_gender_title)).setTypeface(ProductSans);
        ((RadioButton) root.findViewById(R.id.gender_male_radio_button)).setTypeface(ProductSans);
        ((RadioButton) root.findViewById(R.id.gender_female_radio_button)).setTypeface(ProductSans);
        ((TextInputEditText) root.findViewById(R.id.profile_edit_email)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_info_step1)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_skills_title)).setTypeface(ProductSans);
        ((AppCompatCheckBox) root.findViewById(R.id.profile_edit_skills_promo)).setTypeface(ProductSans);
        ((AppCompatCheckBox) root.findViewById(R.id.profile_edit_skills_speaking)).setTypeface(ProductSans);
        ((AppCompatCheckBox) root.findViewById(R.id.profile_edit_skills_tensorFlow)).setTypeface(ProductSans);
        ((AppCompatCheckBox) root.findViewById(R.id.profile_edit_skills_cloud)).setTypeface(ProductSans);
        ((AppCompatCheckBox) root.findViewById(R.id.profile_edit_skills_web)).setTypeface(ProductSans);
        ((AppCompatCheckBox) root.findViewById(R.id.profile_edit_skills_firebase)).setTypeface(ProductSans);
        ((AppCompatCheckBox) root.findViewById(R.id.profile_edit_skills_android)).setTypeface(ProductSans);
        ((TextInputEditText) root.findViewById(R.id.profile_edit_about_me)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_info_step3)).setTypeface(ProductSans);
        ((AppCompatCheckBox) root.findViewById(R.id.profile_edit_step3_info)).setTypeface(ProductSans);
        ((Button)root.findViewById(R.id.profile_edit_proceed_prev)).setTypeface(ProductSans);
        ((Button)root.findViewById(R.id.profile_edit_proceed_next)).setTypeface(ProductSans);

    }
}
