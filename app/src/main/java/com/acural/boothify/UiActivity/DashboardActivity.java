package com.acural.boothify.UiActivity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acural.boothify.MainActivity;
import com.acural.boothify.R;
import com.acural.boothify.roomdb.AppDatabase;

public class DashboardActivity extends AppCompatActivity {

    TextView txtName, txtDistrict, txtAssembly, txtBlock, txtDivision;
    TextView txtTotal, txtMonth, txtToday;
    CardView addMember, cardTwo, cardThree, cardFive, reports;

    ImageView imgT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtName = findViewById(R.id.txtName);
        txtDistrict = findViewById(R.id.txtDistrict);
        txtAssembly = findViewById(R.id.txtAssembly);
        txtBlock = findViewById(R.id.txtBlock);
        txtDivision = findViewById(R.id.txtDivision);
        addMember = findViewById(R.id.addMember);
        cardTwo = findViewById(R.id.cardTwo);
        cardThree = findViewById(R.id.cardThree);
        cardFive = findViewById(R.id.cardFive);
        reports = findViewById(R.id.reports);
        imgT = findViewById(R.id.imgLogout);
        txtTotal = findViewById(R.id.txtTotal);
        txtMonth = findViewById(R.id.txtMonth);
        txtToday = findViewById(R.id.txtToday);

        SharedPreferences pref = getSharedPreferences("BoothifyPref", MODE_PRIVATE);
        txtName.setText(pref.getString("name", ""));
        txtDistrict.setText(pref.getString("district", ""));
        txtAssembly.setText(pref.getString("assembly", ""));
        txtBlock.setText(pref.getString("block", ""));
        txtDivision.setText(pref.getString("division", ""));

        loadCounts();


        addMember.setOnClickListener(v ->
                startActivity(new Intent(this, Add_Member_Activity.class))
        );

        cardTwo.setOnClickListener(v -> {
            Intent intent = new Intent(this, MemberListActivity.class);
            intent.putExtra("type", "viewdata");
            startActivity(intent);
        });

        cardThree.setOnClickListener(v -> {
            Intent intent = new Intent(this, MemberListActivity.class);
            intent.putExtra("type", "existingdata");
            startActivity(intent);
        });

        imgT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ReportsActivity.class));
            }
        });

        cardFive.setOnClickListener(v -> {
            Intent intent = new Intent(this, Add_Member_Activity.class);
            intent.putExtra("type", "existingmember");
            startActivity(intent);
        });

    }


    private void loadCounts() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            int total = db.memberDao().getTotalCount();
            int monthly = db.memberDao().getThisMonthCount();
            int today = db.memberDao().getTodayCount();

            runOnUiThread(() -> {
                txtTotal.setText(String.valueOf(total));
                txtMonth.setText(String.valueOf(monthly));
                txtToday.setText(String.valueOf(today));
            });
        }).start();
    }

    private void showLogoutDialog() {

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setIcon(R.drawable.logout)
                .setCancelable(false)



                .setPositiveButton("Logout", (dialog, which) -> {

                    SharedPreferences preferences =
                            getSharedPreferences(
                                    "BoothifyPref",
                                    MODE_PRIVATE);

                    SharedPreferences.Editor editor =
                            preferences.edit();

                    editor.clear();
                    editor.apply();

                    Intent intent =
                            new Intent(
                                    DashboardActivity.this,
                                    MainActivity.class);

                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);

                    finish();

                    Toast.makeText(
                            DashboardActivity.this,
                            "Logged Out Successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                })

                .setNegativeButton("Cancel",
                        (dialog, which) -> dialog.dismiss())

                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCounts();
    }
}