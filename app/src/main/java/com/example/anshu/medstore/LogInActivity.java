package com.example.anshu.medstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button BtnSignup = (Button) findViewById(R.id.btnSignup);
        Button BtnLogin = (Button) findViewById(R.id.btnLogin);

        BtnLogin.setClickable(false);

        final EditText Username = (EditText)findViewById(R.id.UserName);
        EditText Pass = (EditText)findViewById(R.id.Password);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = Username.getText().toString();
                String p = Username.getText().toString();
                if(u.equals("user") && p.equals("user")){
                    Intent i = new Intent(getApplicationContext(), UserLand.class);
                    startActivity(i);
                }
            }
        });

        BtnSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new user activity
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);

            }
        });
    }
}
