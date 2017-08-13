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


public class LogInActivity extends AppCompatActivity {

    Button loginButton;
    TextView signupLink;
    EditText  userText;
    EditText  passwordText;

    SessionManager session;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private OkHttpClient okhttpclient;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        session = new SessionManager(getApplicationContext());

        userText = (EditText)findViewById(R.id.input_user);
        passwordText = (EditText)findViewById(R.id.input_password);

        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink = (TextView)findViewById(R.id.link_signup);
        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LogInActivity.this,
                R.style.AppTheme_NoActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = userText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        okhttpclient = new OkHttpClient();
        RequestBody Body = new FormBody.Builder()
                .add("format","json")
                .add("UserName", email)
                .add("Password", password)
                .build();

        Request request = new Request.Builder()
                .url(Config.LOGIN_USER).post(Body).build();

        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                System.out.println("^^"+resp);

                try {
                    JSONObject mainObject = new JSONObject(resp);
                    String success = mainObject.getString("success");
                    System.out.println("###"+success);
                    if(success.equals("1")){
                        String username = mainObject.getString("username");
                        System.out.println("###"+success+ username);
                        if(username.equals("admin")){
                            Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            // Add new Flag to start new Activity
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            onActivityResult(REQUEST_SIGNUP, RESULT_OK, intent);
                        }
                        else{
                            Intent i = new Intent(getApplicationContext(), UserLand.class);
                            onActivityResult(REQUEST_SIGNUP, RESULT_OK, i);
                        }
                    }
                    else{
                        onLoginFailed();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 5000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                session.createLoginSession("user");
                startActivity(data);

            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        LogInActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(LogInActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                loginButton.setEnabled(true);
            }
        });

        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(i);

    }

    public boolean validate() {
        boolean valid = true;

        String email = userText.getText().toString();
        String password = passwordText.getText().toString();


        if (password.isEmpty() && email.isEmpty()) {
            passwordText.setError("password or email field is empty");
            valid = false;
        } else {
            passwordText.setError(null);
            userText.setError(null);
        }

        return valid;
    }
}
