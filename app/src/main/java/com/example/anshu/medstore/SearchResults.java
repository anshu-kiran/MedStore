package com.example.anshu.medstore;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchResults extends AppCompatActivity {

    private static final String TAG = LogInActivity.class.getSimpleName();

    private OkHttpClient okhttpclient;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        String message1 = bundle.getString("message1");

        TextView txtView = (TextView) findViewById(R.id.disp);
        txtView.setText(message);

        TextView txtView1 = (TextView) findViewById(R.id.disp1);
        txtView1.setText(message1);
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

                Request request = new Request.Builder().url(Config.SEARCH).post(Body).build();

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
            Intent intent = new Intent(SearchResults.this, SearchResults.class);
            intent.putExtra("message", strArr[1]);
            intent.putExtra("message1", strArr[2]);
            startActivity(intent);
        }
        else if(strArr[0].equals("medicine")){
            Intent intent = new Intent(SearchResults.this, ResultDisplay.class);
            intent.putExtra("ID", strArr[1]);
            System.out.println(">>>>"+strArr[1]);
            intent.putExtra("Table_Name", strArr[2]);
            startActivity(intent);
        }
        else if(strArr[0].equals("tags")){
            if(strArr.length<=1){
                SearchResults.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(SearchResults.this, "No result matches the search query. Please try " +
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
            }
        }
        else{
            SearchResults.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(SearchResults.this, "Error in search query entered.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
