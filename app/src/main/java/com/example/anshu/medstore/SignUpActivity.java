package com.example.anshu.medstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText nameText, addressText, passwordText, phoneText;
    Button signupButton;
    TextView loginLink;

    private OkHttpClient okhttpclient;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameText = (EditText)findViewById(R.id.input_name);
        addressText = (EditText)findViewById(R.id.input_address);
        passwordText = (EditText)findViewById(R.id.input_password);
        phoneText = (EditText)findViewById(R.id.input_phone);
        signupButton = (Button)findViewById(R.id.btn_signup);
        loginLink = (TextView)findViewById(R.id.link_login);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_NoActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String username = nameText.getText().toString();
        String password = passwordText.getText().toString();
        String address = addressText.getText().toString();
        String phone = phoneText.getText().toString();

        // TODO: Implement your own signup logic here.

        okhttpclient = new OkHttpClient();
        RequestBody Body = new FormBody.Builder()
                .add("format","json")
                .add("UserName", username)
                .add("Password", password)
                .add("Address", address)
                .add("Phone", phone)
                .build();

        Request request = new Request.Builder()
                .url(Config.CREATE_USER).post(Body).build();

        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                System.out.println("^^"+resp);

                try {
                    JSONObject mainObject = new JSONObject(resp);
                    String success = mainObject.getString("success");
                    System.out.println("###"+success);
                    if(success.equals("true")){
                        onSignup();
                    }
                    else if(success.equals("false")){
                        onFailed();
                    }
                    else{
                        onSignupFailed();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void onSignupFailed() {

        SignUpActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                signupButton.setEnabled(true);
                Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onFailed() {

        SignUpActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                signupButton.setEnabled(true);
                Toast.makeText(getBaseContext(), "Username already exists. \nTry registering again!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    public void onSignup() {

        SignUpActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "User created.", Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), "Login to enter.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String username = nameText.getText().toString();
        String password = passwordText.getText().toString();
        String address = addressText.getText().toString();
        String phone = phoneText.getText().toString();


        if (username.isEmpty()) {
            nameText.setError("Field is empty");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("Field is empty");
            valid = false;
        } else if(passwordText.getText().length()<=3) {
            passwordText.setError("Password too short");
            valid = false;
        }else {
            passwordText.setError(null);
        }

        if (address.isEmpty()) {
            addressText.setError("Field is empty");
            valid = false;
        } else {
            addressText.setError(null);
        }

        if (phone.isEmpty()) {
            phoneText.setError("Field is empty");
            valid = false;
        } else if(phoneText.getText().length()<10 || phoneText.getText().length()>10 || phoneText.getText().charAt(0)!='9') {
            phoneText.setError("Invalid Phone Number");
            valid = false;
        } else {
            phoneText.setError(null);
        }

        return valid;
    }
}
