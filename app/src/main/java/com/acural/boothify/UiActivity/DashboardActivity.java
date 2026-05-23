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
import com.acural.boothify.roomdb.AppDatabase;

public class DashboardActivity extends AppCompatActivity {

    TextView txtName, txtDistrict, txtAssembly, txtBlock, txtDivision;
    TextView txtTotal, txtMonth, txtToday;
    CardView addMember, cardTwo, cardThree,cardFive,reports;

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

        txtName     = findViewById(R.id.txtName);
        txtDistrict = findViewById(R.id.txtDistrict);
        txtAssembly = findViewById(R.id.txtAssembly);
        txtBlock    = findViewById(R.id.txtBlock);
        txtDivision = findViewById(R.id.txtDivision);
        addMember   = findViewById(R.id.addMember);
        cardTwo     = findViewById(R.id.cardTwo);
        cardThree   = findViewById(R.id.cardThree);
        cardFive   = findViewById(R.id.cardFive);
        reports   = findViewById(R.id.reports);

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

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ReportsActivity.class));
            }
        });

        cardFive.setOnClickListener(v -> {
            Intent intent = new Intent(this, Add_Member_Activity.class);
            intent.putExtra("type","existingmember");
            startActivity(intent);
        });

    }


    private void loadCounts() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            int total   = db.memberDao().getTotalCount();
            int monthly = db.memberDao().getThisMonthCount();
            int today   = db.memberDao().getTodayCount();

            runOnUiThread(() -> {
                txtTotal.setText(String.valueOf(total));
                txtMonth.setText(String.valueOf(monthly));
                txtToday.setText(String.valueOf(today));
            });
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadCounts();
    }
}