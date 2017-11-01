
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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softminds.gdg.R;
import com.softminds.gdg.fragments.EventLists;
import com.softminds.gdg.fragments.HomeSection;
import com.softminds.gdg.fragments.Notify;
import com.softminds.gdg.utils.AdminNotifyHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setNavigationHeader();

        setAdminAccess();

        if(savedInstanceState ==null)
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_home,new HomeSection()).commit();



    }

    private void setAdminAccess() {
        //noinspection ConstantConditions
        AdminNotifyHelper.CheckUser(new AdminNotifyHelper.AdminAccess() {
            @Override
            public void onResult(boolean granted) {
                if(granted){
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    navigationView.getMenu().findItem(R.id.admin_menu).setVisible(true);
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
            ((TextView) header.findViewById(R.id.nav_email)).setText(user.getEmail());
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

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,new HomeSection()).commit();
            if(getSupportActionBar() !=null)
                getSupportActionBar().setTitle(R.string.google_dev);

        } else if (id == R.id.nav_event) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,new EventLists()).commit();
            if(getSupportActionBar() !=null)
                getSupportActionBar().setTitle(R.string.events);

        } else if (id == R.id.nav_notify) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,new Notify()).commit();
            if(getSupportActionBar() !=null)
                getSupportActionBar().setTitle(R.string.notify);

        } else if (id == R.id.nav_add_event) {
            startActivity(new Intent(getApplicationContext(),EventDetails.class));
            if(getSupportActionBar() !=null)
                getSupportActionBar().setTitle(R.string.add_event);

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.share_text));
            startActivity(Intent.createChooser(intent,getString(R.string.send_using)));

        } else if (id == R.id.nav_about_us) {
            startActivity(new Intent(this,AboutUs.class));
        }
     else if (id == R.id.nav_signOut) {
       FirebaseAuth.getInstance().signOut();
       startActivity(new Intent(this,LoginActivity.class));
       finish();
    }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
