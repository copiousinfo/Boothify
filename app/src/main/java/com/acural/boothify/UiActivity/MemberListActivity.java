//package com.acural.boothify.UiActivity;
//
//import android.Manifest;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.acural.boothify.R;
//import com.acural.boothify.adapter.MemberAdapter;
//import com.acural.boothify.model.MemberEntity;
//import com.acural.boothify.roomdb.AppDatabase;
//
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class MemberListActivity extends AppCompatActivity {
//
//    private static final int PERM_REQ = 101;
//
//    RecyclerView recyclerView;
//    MemberAdapter adapter;
//
//    List<MemberEntity> fullList;
//    List<MemberEntity> filteredList;
//
//    TextView statTotalCount, statTodayCount, statMonthCount;
//    EditText etSearch;
//    LinearLayout btnExportExcel;
//
//    String screenType = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_member_list);
//
//        screenType = getIntent().getStringExtra("type");
//        if (screenType == null) screenType = "";
//
//        recyclerView   = findViewById(R.id.recyclerMembers);
//        statTotalCount = findViewById(R.id.statTotalCount);
//        statTodayCount = findViewById(R.id.statTodayCount);
//        statMonthCount = findViewById(R.id.statMonthCount);
//        etSearch       = findViewById(R.id.etSearch);
//        btnExportExcel = findViewById(R.id.btnExportExcel);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        loadMembers();
//        loadStats();
//
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
//            @Override public void afterTextChanged(Editable s) {}
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                filterList(s.toString().trim());
//            }
//        });
//
//        btnExportExcel.setOnClickListener(v -> {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                if (ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            PERM_REQ);
//                    return;
//                }
//            }
//            exportToExcel();
//        });
//    }
//
//    private void loadMembers() {
//        fullList     = AppDatabase.getInstance(this).memberDao().getAllMembers();
//        filteredList = new ArrayList<>(fullList);
//        adapter      = new MemberAdapter(this, filteredList, screenType);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void loadStats() {
//        AppDatabase db = AppDatabase.getInstance(this);
//        statTotalCount.setText(String.valueOf(db.memberDao().getTotalCount()));
//        statTodayCount.setText(String.valueOf(db.memberDao().getTodayCount()));
//        statMonthCount.setText(String.valueOf(db.memberDao().getThisMonthCount()));
//    }
//
//    private void filterList(String query) {
//        filteredList.clear();
//        if (query.isEmpty()) {
//            filteredList.addAll(fullList);
//        } else {
//            String q = query.toLowerCase(Locale.getDefault());
//            for (MemberEntity m : fullList) {
//                if (contains(m.name, q)
//                        || contains(m.memberId, q)
//                        || contains(m.partyRole, q)
//                        || contains(m.block, q)) {
//                    filteredList.add(m);
//                }
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }
//
//    private boolean contains(String field, String query) {
//        return field != null && field.toLowerCase(Locale.getDefault()).contains(query);
//    }
//
//    private void exportToExcel() {
//
//        new Thread(() -> {
//
//            try {
//
//                String fileName = "Boothify_Members_"
//                        + System.currentTimeMillis()
//                        + ".xlsx";
//
//                ContentValues values = new ContentValues();
//
//                values.put(
//                        MediaStore.Downloads.DISPLAY_NAME,
//                        fileName
//                );
//
//                values.put(
//                        MediaStore.Downloads.MIME_TYPE,
//                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
//                );
//
//                values.put(
//                        MediaStore.Downloads.RELATIVE_PATH,
//                        Environment.DIRECTORY_DOWNLOADS
//                );
//
//                Uri uri = getContentResolver().insert(
//                        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
//                        values
//                );
//
//                if (uri == null) {
//
//                    runOnUiThread(() ->
//                            Toast.makeText(
//                                    this,
//                                    "File creation failed",
//                                    Toast.LENGTH_LONG
//                            ).show()
//                    );
//
//                    return;
//                }
//
//                OutputStream outputStream =
//                        getContentResolver().openOutputStream(uri);
//
//                XSSFWorkbook workbook = new XSSFWorkbook();
//
//                XSSFSheet sheet =
//                        workbook.createSheet("Members");
//
//                // Header Row
//                Row header = sheet.createRow(0);
//
//                String[] headers = {
//                        "Sr No",
//                        "Member ID",
//                        "Name",
//                        "Mobile",
//                        "Father",
//                        "Gender",
//                        "DOB",
//                        "Age",
//                        "Block",
//                        "Division",
//                        "District",
//                        "Assembly",
//                        "Voter ID",
//                        "Occupation",
//                        "Education",
//                        "Party Role",
//                        "Created Date"
//                };
//
//                for (int i = 0; i < headers.length; i++) {
//
//                    Cell cell = header.createCell(i);
//
//                    cell.setCellValue(headers[i]);
//                }
//
//                // Data Rows
//                for (int i = 0; i < fullList.size(); i++) {
//
//                    MemberEntity m = fullList.get(i);
//
//                    Row row = sheet.createRow(i + 1);
//
//                    row.createCell(0).setCellValue(i + 1);
//                    row.createCell(1).setCellValue(safe(m.memberId));
//                    row.createCell(2).setCellValue(safe(m.name));
//                    row.createCell(3).setCellValue(safe(m.mobile));
//                    row.createCell(4).setCellValue(safe(m.father));
//                    row.createCell(5).setCellValue(safe(m.gender));
//                    row.createCell(6).setCellValue(safe(m.dob));
//                    row.createCell(7).setCellValue(safe(m.age));
//                    row.createCell(8).setCellValue(safe(m.block));
//                    row.createCell(9).setCellValue(safe(m.division));
//                    row.createCell(10).setCellValue(safe(m.district));
//                    row.createCell(11).setCellValue(safe(m.assembly));
//                    row.createCell(12).setCellValue(safe(m.voterId));
//                    row.createCell(13).setCellValue(safe(m.occupation));
//                    row.createCell(14).setCellValue(safe(m.education));
//                    row.createCell(15).setCellValue(safe(m.partyRole));
//                    row.createCell(16).setCellValue(safe(m.createdDateTime));
//                }
//
//                // Column Widths
//                sheet.setColumnWidth(0, 3000);
//                sheet.setColumnWidth(1, 5000);
//                sheet.setColumnWidth(2, 7000);
//                sheet.setColumnWidth(3, 5000);
//                sheet.setColumnWidth(4, 7000);
//                sheet.setColumnWidth(5, 4000);
//                sheet.setColumnWidth(6, 5000);
//                sheet.setColumnWidth(7, 3000);
//                sheet.setColumnWidth(8, 5000);
//                sheet.setColumnWidth(9, 5000);
//                sheet.setColumnWidth(10, 5000);
//                sheet.setColumnWidth(11, 5000);
//                sheet.setColumnWidth(12, 5000);
//                sheet.setColumnWidth(13, 6000);
//                sheet.setColumnWidth(14, 6000);
//                sheet.setColumnWidth(15, 6000);
//                sheet.setColumnWidth(16, 7000);
//
//                workbook.write(outputStream);
//
//                outputStream.flush();
//
//                outputStream.close();
//
//                workbook.close();
//
//                runOnUiThread(() -> {
//
//                    Toast.makeText(
//                            this,
//                            "Excel Downloaded Successfully",
//                            Toast.LENGTH_LONG
//                    ).show();
//
//                    try {
//
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//
//                        intent.setDataAndType(
//                                uri,
//                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
//                        );
//
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        startActivity(intent);
//
//                    } catch (Exception e) {
//
//                        Toast.makeText(
//                                this,
//                                "No Excel App Found",
//                                Toast.LENGTH_LONG
//                        ).show();
//                    }
//                });
//
//            } catch (Exception e) {
//
//                e.printStackTrace();
//
//                runOnUiThread(() ->
//                        Toast.makeText(
//                                this,
//                                "Error : " + e.getMessage(),
//                                Toast.LENGTH_LONG
//                        ).show()
//                );
//            }
//
//        }).start();
//    }
//
//    private String safe(String val) {
//        return val != null ? val : "";
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int req, String[] perms, int[] results) {
//        super.onRequestPermissionsResult(req, perms, results);
//        if (req == PERM_REQ
//                && results.length > 0
//                && results[0] == PackageManager.PERMISSION_GRANTED) {
//            exportToExcel();
//        } else {
//            Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
//        }
//    }
//}

package com.acural.boothify.UiActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acural.boothify.R;
import com.acural.boothify.adapter.MemberAdapter;
import com.acural.boothify.model.MemberEntity;
import com.acural.boothify.roomdb.AppDatabase;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MemberListActivity extends AppCompatActivity {

    private static final int PERM_REQ = 101;

    RecyclerView recyclerView;
    MemberAdapter adapter;

    // ✅ Teen lists — all, new, existing
    List<MemberEntity> allNewMembers    = new ArrayList<>();
    List<MemberEntity> allExistingMembers = new ArrayList<>();
    List<MemberEntity> filteredList     = new ArrayList<>();

    TextView statTotalCount, statTodayCount, statMonthCount;
    EditText etSearch;
    LinearLayout btnExportExcel;

    // ✅ Tabs
    LinearLayout tabNew, tabExisting;
    TextView txtTabNew, txtTabExisting;

    String screenType = "";
    boolean isExistingTab = false; // ✅ Konsa tab active hai

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        screenType = getIntent().getStringExtra("type");
        if (screenType == null) screenType = "";

        recyclerView   = findViewById(R.id.recyclerMembers);
        statTotalCount = findViewById(R.id.statTotalCount);
        statTodayCount = findViewById(R.id.statTodayCount);
        statMonthCount = findViewById(R.id.statMonthCount);
        etSearch       = findViewById(R.id.etSearch);
        btnExportExcel = findViewById(R.id.btnExportExcel);
        tabNew         = findViewById(R.id.tabNew);
        tabExisting    = findViewById(R.id.tabExisting);
        txtTabNew      = findViewById(R.id.txtTabNew);
        txtTabExisting = findViewById(R.id.txtTabExisting);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMembers();
        loadStats();
        setupTabs();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString().trim());
            }
        });

        btnExportExcel.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERM_REQ);
                    return;
                }
            }
            exportToExcel();
        });
    }

    // ══════════════════════════════════════════
    // LOAD MEMBERS — dono lists alag alag
    // ══════════════════════════════════════════
    private void loadMembers() {
        AppDatabase db = AppDatabase.getInstance(this);

        // ✅ isExistingMember = 0 → New
        allNewMembers = new ArrayList<>();
        for (MemberEntity m : db.memberDao().getAllMembers()) {
            if (m.isExistingMember == 0) allNewMembers.add(m);
            else allExistingMembers = new ArrayList<>(); // reset before adding
        }

        // ✅ Clean approach
        allNewMembers      = new ArrayList<>();
        allExistingMembers = new ArrayList<>();

        for (MemberEntity m : db.memberDao().getAllMembers()) {
            if (m.isExistingMember == 1) allExistingMembers.add(m);
            else allNewMembers.add(m);
        }

        // ✅ Default: New Members tab
        filteredList = new ArrayList<>(allNewMembers);
        adapter = new MemberAdapter(this, filteredList, screenType);
        recyclerView.setAdapter(adapter);
    }

    // ══════════════════════════════════════════
    // TABS SETUP
    // ══════════════════════════════════════════
    private void setupTabs() {
        // Default: New tab active
        setTabActive(true);

        tabNew.setOnClickListener(v -> {
            if (!isExistingTab) return; // already on new tab
            isExistingTab = false;
            setTabActive(true);
            etSearch.setText("");
            filteredList.clear();
            filteredList.addAll(allNewMembers);
            adapter.notifyDataSetChanged();
        });

        tabExisting.setOnClickListener(v -> {
            if (isExistingTab) return; // already on existing tab
            isExistingTab = true;
            setTabActive(false);
            etSearch.setText("");
            filteredList.clear();
            filteredList.addAll(allExistingMembers);
            adapter.notifyDataSetChanged();
        });
    }

    private void setTabActive(boolean newTabActive) {
        if (newTabActive) {
            // New tab active
            tabNew.setBackgroundResource(R.drawable.tab_active_bg);
            tabExisting.setBackgroundResource(R.drawable.tab_inactive_bg);
            txtTabNew.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));
            txtTabExisting.setTextColor(android.graphics.Color.parseColor("#94A3B8"));
        } else {
            // Existing tab active
            tabExisting.setBackgroundResource(R.drawable.tab_active_bg);
            tabNew.setBackgroundResource(R.drawable.tab_inactive_bg);
            txtTabExisting.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));
            txtTabNew.setTextColor(android.graphics.Color.parseColor("#94A3B8"));
        }
    }

    // ══════════════════════════════════════════
    // STATS
    // ══════════════════════════════════════════
    private void loadStats() {
        AppDatabase db = AppDatabase.getInstance(this);
        statTotalCount.setText(String.valueOf(db.memberDao().getTotalCount()));
        statTodayCount.setText(String.valueOf(db.memberDao().getTodayCount()));
        statMonthCount.setText(String.valueOf(db.memberDao().getThisMonthCount()));
    }

    // ══════════════════════════════════════════
    // SEARCH — active tab ki list pe kaam kare
    // ══════════════════════════════════════════
    private void filterList(String query) {
        // ✅ Jo tab active hai uski source list lo
        List<MemberEntity> sourceList = isExistingTab ? allExistingMembers : allNewMembers;

        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(sourceList);
        } else {
            String q = query.toLowerCase(Locale.getDefault());
            for (MemberEntity m : sourceList) {
                if (contains(m.name, q)
                        || contains(m.memberId, q)
                        || contains(m.partyRole, q)
                        || contains(m.block, q)) {
                    filteredList.add(m);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private boolean contains(String field, String query) {
        return field != null && field.toLowerCase(Locale.getDefault()).contains(query);
    }

    // ══════════════════════════════════════════
    // EXCEL EXPORT — active tab ka data
    // ══════════════════════════════════════════
    private void exportToExcel() {
        new Thread(() -> {
            try {
                // ✅ Active tab ka data lo
                List<MemberEntity> exportList = isExistingTab
                        ? allExistingMembers
                        : allNewMembers;

                String tabName = isExistingTab ? "Existing" : "New";
                String fileName = "Boothify_" + tabName + "_Members_"
                        + System.currentTimeMillis() + ".xlsx";

                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                values.put(MediaStore.Downloads.MIME_TYPE,
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                values.put(MediaStore.Downloads.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

                if (uri == null) {
                    runOnUiThread(() -> Toast.makeText(this,
                            "File creation failed", Toast.LENGTH_LONG).show());
                    return;
                }

                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet(tabName + " Members");

                // ✅ Header Row — "Member Type" column add kiya
                Row header = sheet.createRow(0);
                String[] headers = {
                        "Sr No", "Member ID", "Name", "Mobile", "Father",
                        "Gender", "DOB", "Age", "Block", "Division",
                        "District", "Assembly", "Voter ID", "Occupation",
                        "Education", "Party Role", "Member Type", "Created DateTime"
                };
                for (int i = 0; i < headers.length; i++) {
                    header.createCell(i).setCellValue(headers[i]);
                }

                // ✅ Data Rows
                for (int i = 0; i < exportList.size(); i++) {
                    MemberEntity m = exportList.get(i);
                    Row row = sheet.createRow(i + 1);

                    row.createCell(0).setCellValue(i + 1);
                    row.createCell(1).setCellValue(safe(m.memberId));
                    row.createCell(2).setCellValue(safe(m.name));
                    row.createCell(3).setCellValue(safe(m.mobile));
                    row.createCell(4).setCellValue(safe(m.father));
                    row.createCell(5).setCellValue(safe(m.gender));
                    row.createCell(6).setCellValue(safe(m.dob));
                    row.createCell(7).setCellValue(safe(m.age));
                    row.createCell(8).setCellValue(safe(m.block));
                    row.createCell(9).setCellValue(safe(m.division));
                    row.createCell(10).setCellValue(safe(m.district));
                    row.createCell(11).setCellValue(safe(m.assembly));
                    row.createCell(12).setCellValue(safe(m.voterId));
                    row.createCell(13).setCellValue(safe(m.occupation));
                    row.createCell(14).setCellValue(safe(m.education));
                    row.createCell(15).setCellValue(safe(m.partyRole));
                    // ✅ 0/1 ki jagah "New" / "Existing" print hoga
                    row.createCell(16).setCellValue(
                            m.isExistingMember == 1 ? "Existing" : "New"
                    );
                    row.createCell(17).setCellValue(safe(m.createdDateTime));
                }

                // Column Widths
                int[] widths = {3000,5000,7000,5000,7000,4000,5000,3000,
                        5000,5000,5000,5000,5000,6000,6000,6000,5000,7000};
                for (int i = 0; i < widths.length; i++) {
                    sheet.setColumnWidth(i, widths[i]);
                }

                workbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
                workbook.close();

                runOnUiThread(() -> {
                    Toast.makeText(this,
                            tabName + " Members Excel Downloaded!",
                            Toast.LENGTH_LONG).show();
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri,
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(this, "No Excel App Found",
                                Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this,
                        "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private String safe(String val) {
        return val != null ? val : "";
    }

    @Override
    public void onRequestPermissionsResult(int req, String[] perms, int[] results) {
        super.onRequestPermissionsResult(req, perms, results);
        if (req == PERM_REQ && results.length > 0
                && results[0] == PackageManager.PERMISSION_GRANTED) {
            exportToExcel();
        } else {
            Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}