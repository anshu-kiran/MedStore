package com.example.anshu.medstore;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    private OkHttpClient okhttpclient;

    ProgressDialog PD;

    private String url_search = "http://192.168.0.102/MedStoreTest/connect_python.php";

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

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (gridLayout.findLastCompletelyVisibleItemPosition() == medicines.size() - 1) {
                    getMedFromDB(medicines.get(medicines.size() - 1).getId());
                }

            }
        });
*/
       DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.user_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView nvDrawer = (NavigationView)findViewById(R.id.nvView);
        nvDrawer.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                okhttpclient = new OkHttpClient();

                RequestBody Body = new FormBody.Builder().add("Query", query.toString()).build();

                Request request = new Request.Builder().url(url_search).post(Body).build();

                okhttpclient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG,e.getMessage());
                    }


                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        searchRes(response.body().string());
                    }
                });

                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchRes(String res){
        System.out.println(">>>>>>>>"+res);

        JSONArray jsonArray = null;
        String[] strArr= null;
        try {
            jsonArray = new JSONArray(res);
            strArr = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                strArr[i] = jsonArray.getString(i);
            }

            System.out.println(Arrays.toString(strArr));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(strArr[0].equals("levestein_distance")){
            Intent intent = new Intent(UserLand.this, SearchResults.class);
            intent.putExtra("message", strArr[1]);
            intent.putExtra("message1", strArr[2]);
            startActivity(intent);
        }
        else if(strArr[0].equals("medicine")){
            Intent intent = new Intent(UserLand.this, ResultDisplay.class);
            intent.putExtra("ID", strArr[1]);
            System.out.println(">>>>"+strArr[1]);
            intent.putExtra("Table_Name", strArr[2]);
            startActivity(intent);
            /*Intent intent = new Intent(this, MedSearch.class);
            startActivity(intent);*/
        }
        else if(strArr[0].equals("tags")){
            if(strArr.length<=1){
                UserLand.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(UserLand.this, "No result matches the search query. Please try " +
                                "a new search by using another keyword.", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                int n = strArr.length-1;
                String[] arr = new String[n];
                System.arraycopy(strArr,1,arr,0,n);

                for(int i=0; i<n; i++){
                    System.out.println("=="+arr[i]);
                }

                for(String s:arr){
                    System.out.println("!!!"+s);
                }

                Intent intent = new Intent(this, ResultTags.class);
                intent.putExtra("strings", arr);
                startActivity(intent);

                /*Intent intent = new Intent(this, ResultTags.class);
                startActivity(intent);*/
            }
        }
        else{
            UserLand.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(UserLand.this, "Error in search query entered.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void getMedFromDB(int id){
        AsyncTask<Integer, Void, Void> asyncTask = new AsyncTask<Integer, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                PD = new ProgressDialog(UserLand.this);
                PD.setTitle("Please Wait");
                PD.setMessage("Loading...");
                PD.setCancelable(false);
                PD.show();
            }

            @Override
            protected Void doInBackground(Integer... medIds) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http:/192.168.0.102/medstoretest/display.php?id=" + medIds[0])
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
                PD.dismiss();
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
        int id = item.getItemId();

        if (id == R.id.cart) {
            db = new SQLiteHandler(this.getApplicationContext());

            Log.d("Reading: ", "Reading all contacts..");
            List<Product> products = db.getAllProducts();

            for (Product cn : products) {
                String log = "Id: "+cn.getId()+" ,Name: " + cn.getName() + " ,Price: " + cn.getPrice();
                Log.d("Name: ", log);
            }

            Intent i = new Intent(getApplicationContext(), Cart.class);
            startActivity(i);
        }
        else if (id == R.id.signout) {
            session.logoutUser();
            Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.upload){
            Intent i = new Intent(getApplicationContext(), Prescription.class);
            startActivity(i);
        }

        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.user_drawer);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

