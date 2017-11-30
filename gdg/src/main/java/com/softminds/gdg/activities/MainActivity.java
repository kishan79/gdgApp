
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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.test.mock.MockApplication;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softminds.gdg.App;
import com.softminds.gdg.BuildConfig;
import com.softminds.gdg.R;
import com.softminds.gdg.fragments.EventLists;
import com.softminds.gdg.fragments.HomeSection;
import com.softminds.gdg.fragments.Notify;
import com.softminds.gdg.fragments.ProfileFragment;
import com.softminds.gdg.utils.AdminNotifyHelper;
import com.softminds.gdg.utils.AppUpdateChecker;
import com.softminds.gdg.utils.ChangelogLoader;
import com.softminds.gdg.utils.Constants;
import com.softminds.gdg.utils.GdgEvents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
        implements  AppUpdateChecker.UpdateListener,ValueEventListener, NavigationView.OnNavigationItemSelectedListener {

    ProgressBar mProgressbar;
    FrameLayout mFragmentContainer;
    BottomNavigationView bottomView;
    BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        bottomView = findViewById(R.id.bottom_navigation);
        //by default we disable the admin feature that lies in navigation drawer
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProgressbar = findViewById(R.id.content_loading_progress);
        mFragmentContainer = findViewById(R.id.fragment_container_home);

        setNavigationHeader();

        setItemBottomViewListener();

        setBottomView();


        setAdminAccess();

        usersListSet();

        if(getSharedPreferences(Constants.PrefConstants.PREF_NAME,MODE_PRIVATE).getInt(Constants.PrefConstants.LAST_VERSION,BuildConfig.VERSION_CODE-1) < BuildConfig.VERSION_CODE){
            showNewFeatures();
        }

        AppUpdateChecker.CheckUpdate(this);


        if(savedInstanceState ==null)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_home,new HomeSection()).commit();



    }

    private void setBottomView() {
        bottomView.inflateMenu(R.menu.bottom_nav_home);
        bottomView.setOnNavigationItemSelectedListener(itemSelectedListener);
        bottomView.setSelectedItemId(R.id.bottom_nav_home);
    }

    private void setItemBottomViewListener() {
    itemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.bottom_nav_home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,new HomeSection()).commit();
                if(getSupportActionBar() !=null)
                    getSupportActionBar().setTitle(R.string.google_dev);

            } else if (id == R.id.bottom_nav_event) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,new EventLists()).commit();
                if(getSupportActionBar() !=null)
                    getSupportActionBar().setTitle(R.string.events);

            }else if (id == R.id.bottom_nav_profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,new ProfileFragment()).commit();
                if(getSupportActionBar() !=null)
                    getSupportActionBar().setTitle(R.string.profile);

            }
            return true;
        }

    };

    }

    private void showNewFeatures() {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setTitle(getString(R.string.whats_new) + " "+ BuildConfig.VERSION_NAME )
                .setMessage(ChangelogLoader.INSTANCE.loadAll())
                .setCancelable(false)
                .show();

        SharedPreferences preferences = getSharedPreferences(Constants.PrefConstants.PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor =  preferences.edit();
        editor.putInt(Constants.PrefConstants.LAST_VERSION,BuildConfig.VERSION_CODE);
        editor.apply();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
            startActivity(Intent.createChooser(intent, getString(R.string.send_using)));
        } else if (id == R.id.menu_signOut) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else if (id == R.id.menu_about_us) {
            startActivity(new Intent(this,AboutUs.class));
        }

        return true;
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference()
                .child("root")
                .child("events")
                .addValueEventListener(this);
    }

    final ValueEventListener notificationListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                ((App)getApplication()).message = dataSnapshot.getValue(AdminNotifyHelper.class);
                if(mProgressbar.getVisibility() == View.VISIBLE){
                   mFragmentContainer.setVisibility(View.VISIBLE);
                   mProgressbar.setVisibility(View.GONE);
                }
                if(getSupportFragmentManager().findFragmentById(R.id.fragment_container_home) instanceof HomeSection){
                    ((HomeSection) getSupportFragmentManager().findFragmentById(R.id.fragment_container_home)).UpdateMessages();
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void usersListSet() {
        //noinspection ConstantConditions
        String email =  FirebaseAuth.getInstance().getCurrentUser().getEmail();
        //noinspection ConstantConditions
        FirebaseDatabase.getInstance().getReference()
                .child("root")
                .child("userList")
                .child(email.replace('@','_').replace('.','_'))
                .setValue(BuildConfig.VERSION_NAME);
    }

    private void setAdminAccess() {
        //noinspection ConstantConditions
        AdminNotifyHelper.CheckUser(new AdminNotifyHelper.AdminAccess() {
            @Override
            public void onResult(boolean granted) {
                if(granted){
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    //by default we disable the admin feature that lies in navigation drawer
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    Toast.makeText(MainActivity.this, R.string.admin_enabled_toast, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void InvalidAuth() {
                Toast.makeText(getApplicationContext(),R.string.denied,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNavigationHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            ((TextView) header.findViewById(R.id.nav_name)).setText(user.getDisplayName());
           ImageView image =  header.findViewById(R.id.nav_profile_image);

            Glide.with(this)
                    .applyDefaultRequestOptions(RequestOptions.circleCropTransform())
                    .asBitmap()
                    .load(user.getPhotoUrl())
                    .into(image);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notify) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,new Notify()).commit();
            if(getSupportActionBar() !=null)
                getSupportActionBar().setTitle(R.string.notify);

        } else if (id == R.id.nav_add_event) {
            //startActivity(new Intent(getApplicationContext(),EventDetails.class));
            if(getSupportActionBar() !=null)
                getSupportActionBar().setTitle(R.string.add_event);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference()
                .child("root")
                .child("events")
                .removeEventListener(this);
        if(mProgressbar.getVisibility() == View.GONE || mProgressbar.getVisibility() == View.INVISIBLE){
            FirebaseDatabase.getInstance().getReference()
                    .child("root")
                    .child("notifications")
                    .removeEventListener(notificationListener);
        }
    }

    @Override
    public void OnUpdateAvailable(int versionCode, String versionName, String changeLogs, boolean mustUpdate, final String url) {
        if(((App) getApplication()).UpdateSuppress){
            return;
        }
        AlertDialog.Builder baseDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.new_available) + " " + versionName)
                .setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        finish();
                    }
                })
                .setMessage(changeLogs);
        if(mustUpdate){
            baseDialog.setCancelable(false);
            AlertDialog dialog =  baseDialog.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        else {
            baseDialog.setCancelable(true)
                    .setNegativeButton(R.string.ignore, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            ((App)getApplication()).UpdateSuppress = true;
                        }
                    }).create().show();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){
            ((App)getApplication()).events = new ArrayList<>();
            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                ((App) getApplication()).events.add(dataSnapshot1.getValue(GdgEvents.class));

            Collections.sort(((App) getApplication()).events, new Comparator<GdgEvents>() {
                @Override
                public int compare(GdgEvents gdgEvents, GdgEvents t1) {
                    if(t1.getTime() == gdgEvents.getTime())
                        return 0;
                    else return gdgEvents.getTime() > t1.getTime() ? -1 : 1;
                }
            });

            if(getSupportFragmentManager().findFragmentById(R.id.fragment_container_home) instanceof HomeSection){
                ((HomeSection) getSupportFragmentManager().findFragmentById(R.id.fragment_container_home)).UpdateEvents();
            }
            if(mProgressbar.getVisibility() == View.VISIBLE) {
                FirebaseDatabase.getInstance().getReference()
                        .child("root")
                        .child("notifications")
                        .addValueEventListener(notificationListener);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
