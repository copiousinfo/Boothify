package com.acural.boothify.UiActivity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.view.View;

import android.content.SharedPreferences;
import android.widget.TextView;


import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acural.boothify.R;

public class DashboardActivity extends AppCompatActivity {
    TextView txtName,
            txtDistrict,
            txtAssembly,
            txtBlock,
            txtDivision;

    CardView addMember;

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


        SharedPreferences pref =
                getSharedPreferences("BoothifyPref",
                        MODE_PRIVATE);

        String name = pref.getString("name", "");
        String district = pref.getString("district", "");
        String assembly = pref.getString("assembly", "");
        String block = pref.getString("block", "");
        String division = pref.getString("division", "");

        txtName.setText(name);
        txtDistrict.setText(district);
        txtAssembly.setText(assembly);
        txtBlock.setText(block);
        txtDivision.setText(division);


        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashboardActivity.this,
                        Add_Member_Activity.class);

                startActivity(intent);

            }
        });
    }
}