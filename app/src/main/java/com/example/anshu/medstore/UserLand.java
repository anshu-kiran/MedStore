package com.example.anshu.medstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserLand extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle drawerToggle;
    View parentView;

    private static final String TAG = LogInActivity.class.getSimpleName();
    private  List<Medicine> medicines;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    private MedicineAdapter adapter;

    SessionManager session;

    SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_land);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        medicines = new ArrayList<>();
        getMedFromDB(0);

        gridLayout = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayout);

        adapter = new MedicineAdapter(this,medicines);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (gridLayout.findLastCompletelyVisibleItemPosition() == medicines.size() - 1) {
                    getMedFromDB(medicines.get(medicines.size() - 1).getId());
                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLand.this, Search.class));
            }
        });

        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.user_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView nvDrawer = (NavigationView)findViewById(R.id.nvView);
        nvDrawer.setNavigationItemSelectedListener(this);
    }

    private void getMedFromDB(int id){
        AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... medIds) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http:/192.168.110.120/medstoretest/display.php?id=" + medIds[0])
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        Medicine medicine = new Medicine(object.getInt("id"), object.getString("med_name"),
                                object.getString("med_image"), object.getString("med_price"));

                        UserLand.this.medicines.add(medicine);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };

        asyncTask.execute(id);
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


        if (id == R.id.cart) {
            db = new SQLiteHandler(this.getApplicationContext());

            Log.d("Reading: ", "Reading all contacts..");
            List<Product> products = db.getAllProducts();

            for (Product cn : products) {
                String log = "Id: "+cn.getId()+" ,Name: " + cn.getName() + " ,Price: " + cn.getPrice();
                // Writing Contacts to log
                Log.d("Name: ", log);
            }

            Intent i = new Intent(getApplicationContext(), Cart.class);
            startActivity(i);


        } else if (id == R.id.signout) {
            session.logoutUser();

            //Snackbar.make(getWindow().getDecorView().getRootView(),"Logged out", Snackbar.LENGTH_LONG).show();
            Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.upload){
            Intent i = new Intent(getApplicationContext(), Prescription.class);
            startActivity(i);

        }

        else if (id == R.id.about){
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

