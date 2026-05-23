//package com.acural.boothify.UiActivity;
//
//import android.os.Bundle;
//import android.widget.ImageView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.acural.boothify.Fragment.FirstStep_Fragment;
//import com.acural.boothify.R;
//
//public class Add_Member_Activity extends AppCompatActivity {
//    ImageView btnBack;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_add_member);
//
//        btnBack = findViewById(R.id.btnBack);
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frameContainer, new FirstStep_Fragment())
//                .commit();
//
//        btnBack.setOnClickListener(v -> onBackPressed());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//}

package com.acural.boothify.UiActivity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acural.boothify.Fragment.FirstStep_Fragment;
import com.acural.boothify.R;

public class Add_Member_Activity extends AppCompatActivity {

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_member);

        btnBack = findViewById(R.id.btnBack);

        // ✅ Intent se type lo
        String type = getIntent().getStringExtra("type");
        boolean isExisting = "existingmember".equals(type);

        // ✅ Fragment ko bundle se type bhejo
        FirstStep_Fragment fragment = new FirstStep_Fragment();
        Bundle args = new Bundle();
        args.putBoolean("isExistingMember", isExisting);
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameContainer, fragment)
                .commit();

        btnBack.setOnClickListener(v -> onBackPressed());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}