package com.example.anshu.medstore;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class SignUpActivity extends AppCompatActivity {

    //creation of OkHttpClient, request
    private OkHttpClient okhttpclient;
    private Request request;

    //url
    private String url_create_user = "http://192.168.0.102/MedStoreTest/create_user.php";



    //Parameters
    EditText User, Pass, Add, PhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Edit Text
        User = (EditText) findViewById(R.id.UserName);
        Pass = (EditText) findViewById(R.id.Password);
        Add = (EditText) findViewById(R.id.Address);
        PhoneNo = (EditText) findViewById(R.id.Phone);

        Button AddUser = (Button) findViewById(R.id.btnReg);
        AddUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // initalize http client
                okhttpclient = new OkHttpClient();
                //initialize http request
                RequestBody Body = new FormBody.Builder()
                        .add("format","json")
                        .add("UserName", User.getText().toString())
                        .add("Password", Pass.getText().toString())
                        .add("Address",Add.getText().toString() )
                        .add("Phone",PhoneNo.getText().toString() )
                        .build();

                Request request = new Request.Builder()
                        .url(url_create_user).post(Body).build();

                //execute the request
                okhttpclient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG,e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i(TAG,response.body().string());
                    }
                });

                Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Login to Enter", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(i);

            }
        });
    }
}
