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

public class OrderDisplay extends AppCompatActivity {

    private static final String TAG = OrderDisplay.class.getSimpleName();
    private List<Order> orderModel;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    private OrderAdapter adapter;

    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);

        recyclerView = (RecyclerView) findViewById(R.id.orderrecyclerview);
        orderModel = new ArrayList<>();


        gridLayout = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayout);

        adapter = new OrderAdapter(this, orderModel);
        recyclerView.setAdapter(adapter);
        getOrdersFromDB();
    }

    private void getOrdersFromDB(){
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                PD = new ProgressDialog(OrderDisplay.this);
                PD.setTitle("Please Wait");
                PD.setMessage("Loading...");
                PD.setCancelable(false);
                PD.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Config.ORDER_DISPLAY)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);

                        Order order = new Order(object.getInt("id"), object.getString("username"),
                                object.getString("orders"), object.getInt("cost"));

                        OrderDisplay.this.orderModel.add(order);
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

