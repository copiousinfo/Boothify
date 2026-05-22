package com.acural.boothify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acural.boothify.UiActivity.DashboardActivity;

public class MainActivity extends AppCompatActivity {
    EditText etMobile, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Initialize Views
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Login Button Click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = etMobile.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Check Credentials
                if (mobile.equals("9958596363") && password.equals("1234")) {

                    Toast.makeText(MainActivity.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT).show();

                    // Open Dashboard Activity
                    Intent intent = new Intent(MainActivity.this,
                            DashboardActivity.class);

                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(MainActivity.this,
                            "Invalid Mobile Number or Password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}