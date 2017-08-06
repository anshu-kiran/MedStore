package com.example.anshu.medstore;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

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
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultDisplay extends AppCompatActivity {

    private static final String TAG = UserLand.class.getSimpleName();
    private List<Medicine> modelMedicines;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    private MedicineAdapter adapter;

    private OkHttpClient okhttpclient;
    private Request request;

    private String url_search = "http://192.168.0.102/MedStoreTest/connect_python.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        Bundle bundle = getIntent().getExtras();
        String ids = bundle.getString("ID");
        String table = bundle.getString("Table_Name");

        recyclerView = (RecyclerView)findViewById(R.id.searchshow);
        modelMedicines = new ArrayList<>();
        getMedDB(ids, table);

        gridLayout = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayout);

        adapter = new MedicineAdapter(this, modelMedicines);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(ResultDisplay.this, SearchResults.class);
            intent.putExtra("message", strArr[1]);
            intent.putExtra("message1", strArr[2]);
            startActivity(intent);
        }
        else if(strArr[0].equals("medicine")){
            Intent intent = new Intent(ResultDisplay.this, ResultDisplay.class);
            intent.putExtra("ID", strArr[1]);
            System.out.println(">>>>"+strArr[1]);
            intent.putExtra("Table_Name", strArr[2]);
            startActivity(intent);
            /*Intent intent = new Intent(this, MedSearch.class);
            startActivity(intent);*/
        }
    }

    private void getMedDB(String idl, String table){

        AsyncTask<String, String, Void> asyncTask = new AsyncTask<String, String, Void>() {
            @Override
            protected Void doInBackground(String... params) {

                String ids = params[0];
                System.out.println("^^^^^"+ids);
                String tables = params[1];
                System.out.println("&&&&&&&&"+tables);

                HttpUrl url = new HttpUrl.Builder()
                        .scheme("http")
                        .host("192.168.0.102")
                        .addPathSegment("medstoretest")
                        .addPathSegment("search.php")
                        .addQueryParameter("tbl", tables)
                        .addQueryParameter("id", ids)
                        .build();

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);
                        System.out.println(object);

                        Medicine modelMedicine = new Medicine(object.getInt("id"), object.getString("med_name"),
                                object.getString("med_image"), object.getString("med_price"));

                        ResultDisplay.this.modelMedicines.add(modelMedicine);
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

        asyncTask.execute(idl, table);
    }

}