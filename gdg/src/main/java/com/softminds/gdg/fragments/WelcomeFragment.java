package com.softminds.gdg.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softminds.gdg.App;
import com.softminds.gdg.R;
import com.softminds.gdg.activities.MainActivity;
import com.softminds.gdg.utils.AppUsers;
import com.softminds.gdg.utils.Constants;
import com.softminds.gdg.utils.ProfileHelper;

public class WelcomeFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final int FADE_TIME = 1700;
    View root;

    GoogleApiClient mApiClient;

    FirebaseAuth mAuth;

    public WelcomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_welcome, container, false);
        mAuth = FirebaseAuth.getInstance();
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViewTypeFace();
        startAppearAnimation();
        setButtonListeners();
    }

    private void startAppearAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(FADE_TIME);
        root.findViewById(R.id.welcome_sign_in_button).startAnimation(animation);
        root.findViewById(R.id.welcome_text_message).startAnimation(animation);
        root.findViewById(R.id.welcome_gdg_logo).startAnimation(animation);
    }

    private void setViewTypeFace() {
        //noinspection ConstantConditions
        Typeface productSans = Typeface.createFromAsset(getActivity().getAssets(), Constants.PathConstants.PRODUCT_SANS_FONT);
        ((Button) root.findViewById(R.id.welcome_sign_in_button)).setTypeface(productSans);
        ((TextView) root.findViewById(R.id.welcome_text_message)).setTypeface(productSans);
        ((TextView) root.findViewById(R.id.welcome_login_text)).setTypeface(productSans);
    }

    private void setButtonListeners() {
        root.findViewById(R.id.welcome_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FadeView();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startLoginFlowWithGoogle();
                        //this runnable will run only after views have faded
                    }
                },FADE_TIME);
            }
        });
    }

    private void FadeView() {
        AlphaAnimation animation = new AlphaAnimation(1.0f,0.0f);
        animation.setDuration(FADE_TIME);
        root.findViewById(R.id.welcome_sign_in_button).startAnimation(animation);
        root.findViewById(R.id.welcome_text_message).startAnimation(animation);
        root.findViewById(R.id.welcome_gdg_logo).startAnimation(animation);
    }

    private void startLoginFlowWithGoogle() {
        showLoginProgress();
        ((TextView) root.findViewById(R.id.welcome_login_text)).setText(R.string.connecting_with_google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("702840488366-371h2kr5cbnq5bmsne0pphhhkifacc5r.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //noinspection ConstantConditions
        mApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mApiClient),999);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(googleSignInResult.isSuccess()){
                firebaseAuthWithGoogle(googleSignInResult.getSignInAccount());
            }else{
                Toast.makeText(getContext(),R.string.auth_error,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        ((TextView) root.findViewById(R.id.welcome_login_text)).setText(R.string.authenticating_you);
        mAuth.signInWithCredential(GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ((TextView) root.findViewById(R.id.welcome_login_text)).setText(R.string.database_check);
                            ProfileHelper.check(new ProfileHelper.CheckListener() {
                                @Override
                                public void OnProfileExist(String profileUrl) {
                                    ((TextView) root.findViewById(R.id.welcome_login_text)).setText(R.string.gathering_profile);
                                    FirebaseDatabase.getInstance().getReference(profileUrl)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    ((TextView) root.findViewById(R.id.welcome_login_text)).setText(R.string.done);
                                                    //noinspection ConstantConditions
                                                    ((App)getActivity().getApplication()).appUser = dataSnapshot.getValue(AppUsers.class);
                                                    startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                                                    getActivity().finish();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    //ignore this error : This should not happen mostly
                                                }
                                            });
                                }

                                @Override
                                public void OnNotExist() {
                                    //noinspection ConstantConditions
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.login_fragment_container,new ProfileSetter()).commit();
                                }
                            });
                        }else{
                            Toast.makeText(getContext(),R.string.something_went_wrong,Toast.LENGTH_SHORT).show();
                            removeLoginProgress();
                        }
                    }
                });
    }

    private void showLoginProgress() {
        //noinspection ConstantConditions
        if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            //noinspection ConstantConditions
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.logging_in);
        root.findViewById(R.id.welcome_sign_in_button).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.welcome_text_message).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.welcome_gdg_logo).setVisibility(View.INVISIBLE);

        root.findViewById(R.id.welcome_login_progress).setVisibility(View.VISIBLE);
        root.findViewById(R.id.welcome_login_text).setVisibility(View.VISIBLE);
    }

    private void removeLoginProgress(){
        //noinspection ConstantConditions
        if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
            //noinspection ConstantConditions
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.welcome);

        root.findViewById(R.id.welcome_sign_in_button).setVisibility(View.VISIBLE);
        root.findViewById(R.id.welcome_text_message).setVisibility(View.VISIBLE);
        root.findViewById(R.id.welcome_gdg_logo).setVisibility(View.VISIBLE);

        root.findViewById(R.id.welcome_login_progress).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.welcome_login_text).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), R.string.google_play_service_error, Toast.LENGTH_SHORT).show();
        removeLoginProgress();
    }
}
