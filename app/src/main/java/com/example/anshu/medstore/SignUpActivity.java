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

        // initalize http client
        okhttpclient = new OkHttpClient();
        //initialize http request
        RequestBody Body = new FormBody.Builder()
                .add("format","json")
                .add("UserName", username)
                .add("Password", password)
                .add("Address", address)
                .add("Phone", phone)
                .build();

        Request request = new Request.Builder()
                .url(Config.CREATE_USER).post(Body).build();

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

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Login to Enter", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(i);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = nameText.getText().toString();
        String password = passwordText.getText().toString();
        String address = addressText.getText().toString();
        String phone = phoneText.getText().toString();


        if (username.isEmpty()) {
            nameText.setError("field empty");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("field empty");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (address.isEmpty()) {
            addressText.setError("field is empty");
            valid = false;
        } else {
            addressText.setError(null);
        }

        if (phone.isEmpty()) {
            phoneText.setError("field is empty");
            valid = false;
        } else {
            phoneText.setError(null);
        }

        return valid;
    }
}
