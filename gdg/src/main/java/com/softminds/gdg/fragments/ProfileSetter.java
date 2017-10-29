package com.softminds.gdg.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.internal.SignInHubActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.softminds.gdg.R;
import com.softminds.gdg.activities.MainActivity;
import com.softminds.gdg.utils.AppUsers;
import com.softminds.gdg.utils.Constants;


public class ProfileSetter extends Fragment {


    View root;
    ActionBar actionBar;
    Typeface ProductSans;
    FloatingActionButton next,back;
    ConstraintLayout step0,step2,step3;
    LinearLayout step1;

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
        setNextFabListener();
        setBackFabListener();

    }

    private void setBackFabListener() {
        switch (currentStep){
            case 1:
                fadeView(step1);
                appearView(step2);
                currentStep = 0;
                back.setEnabled(false);
                if (actionBar != null) {
                    actionBar.setTitle(R.string.profile_complete);
                }
                break;
            case 2:
                fadeView(step2);
                appearView(step1);
                currentStep = 1;
                if (actionBar != null) {
                    actionBar.setTitle(R.string.profile_complete_step1);
                }
                break;
            case 3:
                fadeView(step3);
                appearView(step2);
                currentStep =2;
                if (actionBar != null) {
                    actionBar.setTitle(R.string.profile_complete_step2);
                }
                break;
        }
    }

    private void setNextFabListener() {
        switch (currentStep){
            case 1:
                if(!validate(1)){
                    break;
                }
                fadeView(step1);
                appearView(step2);
                currentStep = 2;
                if (actionBar != null) {
                    actionBar.setTitle(R.string.profile_complete_step2);
                }
                break;
            case 2:
                if(!validate(2)){
                    break;
                }
                fadeView(step2);
                appearView(step3);
                if (actionBar != null) {
                    actionBar.setTitle(R.string.profile_complete_step3);
                }
                currentStep = 3;
            case 3:
                if(!validate(3)){
                    break;
                }
                fadeView(step3);
                startActivity(new Intent(getContext(),MainActivity.class));
            }
    }

    private boolean validate(int i) {
        // TODO: 29/10/17 Validate the step values as denoted by the 'i'
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
        ((TextView) root.findViewById(R.id.profile_edit_message)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_message_salutation)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_proceed)).setTypeface(ProductSans);
        ((TextInputEditText) root.findViewById(R.id.profile_edit_name)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_gender_title)).setTypeface(ProductSans);
        ((RadioButton) root.findViewById(R.id.gender_male_radio_button)).setTypeface(ProductSans);
        ((RadioButton) root.findViewById(R.id.gender_female_radio_button)).setTypeface(ProductSans);
        ((TextInputEditText) root.findViewById(R.id.profile_edit_email)).setTypeface(ProductSans);
        ((TextView) root.findViewById(R.id.profile_edit_info_step1)).setTypeface(ProductSans);

    }
}
