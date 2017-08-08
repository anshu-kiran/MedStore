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

public class PresDisplay extends AppCompatActivity {

    private static final String TAG = UserLand.class.getSimpleName();
    private List<Pres> presModel;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    private PresAdapter adapter;

    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pres_display);

        recyclerView = (RecyclerView) findViewById(R.id.presview);
        presModel = new ArrayList<>();


        gridLayout = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayout);

        adapter = new PresAdapter(this, presModel);
        recyclerView.setAdapter(adapter);
        getPresFromDB();
    }

    private void getPresFromDB(){
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                PD = new ProgressDialog(PresDisplay.this);
                PD.setTitle("Please Wait");
                PD.setMessage("Loading...");
                PD.setCancelable(false);
                PD.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://192.168.0.102/medstoretest/pres_display.php")
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        Pres pres = new Pres(object.getInt("id"), object.getString("username"),
                                object.getString("url"));

                        System.out.println("%%%%%"+pres);

                        PresDisplay.this.presModel.add(pres);
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
