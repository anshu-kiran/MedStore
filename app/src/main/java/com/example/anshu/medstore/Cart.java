package com.example.anshu.medstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Cart extends AppCompatActivity {

    SQLiteHandler db;
    RecyclerView recyclerView;
    RecycleAdapter recycler;
    List<Product> product;

    Button chckOut, clear;

    SessionManager session;
    String usern;

    private OkHttpClient okhttpclient;
    private String url_order_insert = "http://192.168.0.103/MedStoreTest/order_insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        product =new ArrayList<Product>();
        recyclerView = (RecyclerView) findViewById(R.id.cartrecyclerview);

        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(this.getApplicationContext());

        product =  db.getAllProducts();
        recycler =new RecycleAdapter(product);

        chckOut = (Button)findViewById(R.id.chckOut);
        chckOut.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("Reading: ", "Reading medicine names..");
                List<Product> products = db.getMedName();
                ArrayList<String> array = new ArrayList<String>();
                for (Product cn : product) {
                    String log = cn.getName();
                    array.add(log);
                }
                //System.out.println(array);
                StringBuilder buffer = new StringBuilder();
                for (String each : array)
                    buffer.append(",").append(each);
                String joined = buffer.deleteCharAt(0).toString();
                System.out.println(joined);

                int price = 0;
                ArrayList<Integer> tprice = new ArrayList<Integer>();
                List<Product> totalPrice = db.getTotalPrice();
                for (Product cn : product) {
                    String logs = cn.getPrice();
                    price = Integer.parseInt(logs);
                    tprice.add(price);
                }
                System.out.println(tprice);
                int totalprice = 0;
                for (int i : tprice)
                    totalprice += i;
                System.out.println("Total price is : " + totalprice);
                String totalprices = String.valueOf(totalprice);

                HashMap<String, String> user = session.getUserDetails();
                // name
                String usern = user.get(SessionManager.KEY_NAME);
                System.out.println(usern);

                okhttpclient = new OkHttpClient();
                //initialize http request
                RequestBody Body = new FormBody.Builder()
                        .add("format","json")
                        .add("username", usern)
                        .add("orders", joined)
                        .add("cost", totalprices)
                        .build();

                Request request = new Request.Builder()
                        .url(url_order_insert).post(Body).build();

                //execute the request
                okhttpclient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("TAG",e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("TAG",response.body().string());
                    }
                });

                SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                db.deleteAllProduct();
                Intent i = new Intent(getApplicationContext(), UserLand.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Checked Out", Toast.LENGTH_SHORT);
            }
        });

        clear = (Button)findViewById(R.id.btnClr);
        clear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                db.deleteAllProduct();

                Intent i = new Intent(getApplicationContext(), Cart.class);
                startActivity(i);
            }
        });

        Log.i("Ddata",""+product);
        RecyclerView.LayoutManager reLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);
    }
}