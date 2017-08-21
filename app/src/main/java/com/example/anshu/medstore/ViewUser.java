package com.example.anshu.medstore;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewUser extends AppCompatActivity {

    private static final String TAG = UserLand.class.getSimpleName();
    private List<User> userModel;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    private UserAdapter adapter;

    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        recyclerView = (RecyclerView) findViewById(R.id.userview);
        userModel = new ArrayList<>();


        gridLayout = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayout);

        adapter = new UserAdapter(this, userModel);
        recyclerView.setAdapter(adapter);
        getDB();
    }

    private void getDB(){
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                PD = new ProgressDialog(ViewUser.this);
                PD.setTitle("Please Wait");
                PD.setMessage("Loading...");
                PD.setCancelable(false);
                PD.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Config.VIEW_USER)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        User user = new User(object.getString("username"),
                                object.getString("address"), object.getString("phone"));

                        ViewUser.this.userModel.add(user);
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
        } ;
        asyncTask.execute();

    }
}