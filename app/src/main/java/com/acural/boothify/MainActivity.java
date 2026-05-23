package com.acural.boothify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize Views
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Shared Pref
        sharedPreferences =
                getSharedPreferences("BoothifyPref",
                        MODE_PRIVATE);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars());

                    v.setPadding(systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);

                    return insets;
                });

        // ================= AUTO LOGIN =================

        boolean isLogin =
                sharedPreferences.getBoolean(
                        "isLogin",
                        false);

        if (isLogin) {

            Intent intent =
                    new Intent(MainActivity.this,
                            DashboardActivity.class);

            startActivity(intent);
            finish();
        }

        // ================= LOGIN BUTTON =================

        btnLogin.setOnClickListener(v -> {

            String mobile =
                    etMobile.getText().toString().trim();

            String password =
                    etPassword.getText().toString().trim();

            // EMPTY CHECK

            if (mobile.isEmpty()) {

                etMobile.setError(
                        "Enter Mobile Number");

                etMobile.requestFocus();

                return;
            }

            if (password.isEmpty()) {

                etPassword.setError(
                        "Enter Password");

                etPassword.requestFocus();

                return;
            }



            if (mobile.equals("9999116953")
                    && password.equals("1234")) {

                saveUserData(
                        "Mr. A.K Singh",
                        "Satna",
                        "Satna",
                        "Satna City Block",
                        "Rewa"
                );

                openDashboard();
            }



            else if (mobile.equals("96257 95930")
                    && password.equals("4321")) {

                saveUserData(
                        "Mr. Rahul Sharma",
                        "Sidhi",
                        "Sidhi",
                        "Sidhi City",
                        "Rewa"
                );

                openDashboard();
            }

            else {

                Toast.makeText(
                        MainActivity.this,
                        "Invalid Mobile Number or Password",
                        Toast.LENGTH_SHORT
                ).show();
            }

        });

    }

    // ================= SAVE USER DATA =================

    private void saveUserData(String name,
                              String district,
                              String assembly,
                              String block,
                              String division) {

        SharedPreferences.Editor editor =
                sharedPreferences.edit();

        editor.putString("name", name);
        editor.putString("district", district);
        editor.putString("assembly", assembly);
        editor.putString("block", block);
        editor.putString("division", division);

        editor.putBoolean("isLogin", true);

        editor.apply();
    }

    // ================= OPEN DASHBOARD =================

    private void openDashboard() {

        Toast.makeText(
                MainActivity.this,
                "Login Successful",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent =
                new Intent(MainActivity.this,
                        DashboardActivity.class);

        startActivity(intent);

        finish();
    }
}