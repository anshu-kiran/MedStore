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

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    SQLiteHandler db;
    RecyclerView recyclerView;
    RecycleAdapter recycler;
    List<Product> product;

    Button chckOut, clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        product =new ArrayList<Product>();
        recyclerView = (RecyclerView) findViewById(R.id.cartrecyclerview);

        SQLiteHandler db = new SQLiteHandler(this.getApplicationContext());
        product =  db.getAllProducts();
        recycler =new RecycleAdapter(product);

        chckOut = (Button)findViewById(R.id.chckOut);
        chckOut.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
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