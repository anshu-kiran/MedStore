package com.example.anshu.medstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class UserLand extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle drawerToggle;
    View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_land);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.user_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView nvDrawer = (NavigationView)findViewById(R.id.nvView);
        nvDrawer.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.user_drawer);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pres) {
            Toast.makeText(this, "Opens the list of prescription drugs", Toast.LENGTH_SHORT).show();
            // Handle camera action
        } else if (id == R.id.nopres){
            Toast.makeText(this, "Opens the list of non-prescription drugs", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.cart) {
            Toast.makeText(this, "Opens cart", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.signout) {
            startActivity(new Intent(UserLand.this, MainActivity.class));
            //Snackbar.make(getWindow().getDecorView().getRootView(),"Logged out", Snackbar.LENGTH_LONG).show();
            Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.about){
            //opens about dialog
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Developed by Anshu Kiran Sharma and Namrata Giri").setTitle(R.string.dialog_title);
            alertDialogBuilder.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            closeContextMenu();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.user_drawer);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }




}

