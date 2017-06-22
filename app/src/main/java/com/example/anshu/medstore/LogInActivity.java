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


public class LogInActivity extends AppCompatActivity {

    Button loginButton;
    TextView signupLink;
    EditText userText;
    EditText passwordText;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        session = new SessionManager(getApplicationContext());

        userText = (EditText)findViewById(R.id.input_user);
        passwordText = (EditText)findViewById(R.id.input_password);

        /*Toast.makeText(getApplicationContext(),
                "User Login Status: " + session.isLoggedIn(),
                Toast.LENGTH_LONG).show();*/

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
        progressDialog.setMessage("Logging In. Please wait...");
        progressDialog.show();

        String email = userText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        if(email.equals("user") && password.equals("user")){
            session.createLoginSession("user");
            Intent intent = new Intent(getApplicationContext(), UserLand.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            onActivityResult(REQUEST_SIGNUP, RESULT_OK, intent);
        }
        else if(email.equals("admin") && password.equals("admin")){
            Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            onActivityResult(REQUEST_SIGNUP, RESULT_OK, intent);
        }
        else{
            onLoginFailed();
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                startActivity(data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back
        moveTaskToBack(false);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(i);

        loginButton.setEnabled(true);
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