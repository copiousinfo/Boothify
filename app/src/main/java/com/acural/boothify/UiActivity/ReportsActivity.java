package com.acural.boothify.UiActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acural.boothify.R;
import com.acural.boothify.adapter.ReportAdapter;
import com.acural.boothify.model.MemberEntity;
import com.acural.boothify.roomdb.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportsActivity extends AppCompatActivity {

    // Stats
    TextView txtTotal, txtToday, txtMonth;
    TextView txtFilteredCount, txtFilterLabel;

    // Filters
    EditText etFromDate, etToDate, etAreaSearch;
    LinearLayout btnClearFilter;
    Spinner spAreaType;

    // RecyclerView
    RecyclerView recyclerView;
    ReportAdapter adapter;

    // Data
    List<MemberEntity> allMembers = new ArrayList<>();
    List<MemberEntity> filteredList = new ArrayList<>();

    String fromDate = "", toDate = "", areaQuery = "", selectedAreaType = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        initViews();
        loadData();
        setupAreaTypeSpinner();
        setupDatePickers();
        setupAreaSearch();
        setupClearFilter();
    }

    // ══════════════════════════════════════════
    // INIT
    // ══════════════════════════════════════════
    private void initViews() {
        txtTotal         = findViewById(R.id.txtReportTotal);
        txtToday         = findViewById(R.id.txtReportToday);
        txtMonth         = findViewById(R.id.txtReportMonth);
        txtFilteredCount = findViewById(R.id.txtFilteredCount);
        txtFilterLabel   = findViewById(R.id.txtFilterLabel);
        etFromDate       = findViewById(R.id.etFromDate);
        etToDate         = findViewById(R.id.etToDate);
        etAreaSearch     = findViewById(R.id.etAreaSearch);
        btnClearFilter   = findViewById(R.id.btnClearFilter);
        spAreaType       = findViewById(R.id.spAreaType);
        recyclerView     = findViewById(R.id.recyclerReports);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // ══════════════════════════════════════════
    // LOAD DATA
    // ══════════════════════════════════════════
    private void loadData() {
        AppDatabase db = AppDatabase.getInstance(this);
        allMembers = db.memberDao().getAllMembers();
        filteredList = new ArrayList<>(allMembers);

        // Stats
        txtTotal.setText(String.valueOf(db.memberDao().getTotalCount()));
        txtToday.setText(String.valueOf(db.memberDao().getTodayCount()));
        txtMonth.setText(String.valueOf(db.memberDao().getThisMonthCount()));

        // Adapter
        adapter = new ReportAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);
        updateFilteredCount();
    }

    // ══════════════════════════════════════════
    // AREA TYPE SPINNER
    // ══════════════════════════════════════════

    private void setupAreaTypeSpinner() {
        String[] areaTypes = {"All Areas", "Block", "District", "Assembly", "Division"};

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, areaTypes) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(android.R.id.text1);
                tv.setTextColor(android.graphics.Color.BLACK);
                tv.setBackgroundColor(android.graphics.Color.WHITE);
                tv.setTextSize(13f);
                tv.setPadding(8, 0, 8, 0);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                TextView tv = v.findViewById(android.R.id.text1);
                tv.setTextColor(android.graphics.Color.BLACK);
                tv.setBackgroundColor(android.graphics.Color.WHITE);
                tv.setTextSize(13f);
                tv.setPadding(24, 16, 24, 16);
                return v;
            }
        };

        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAreaType.setAdapter(spinAdapter);
        spAreaType.setBackgroundColor(android.graphics.Color.WHITE);

        spAreaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (view != null) {
                    ((TextView) view).setTextColor(android.graphics.Color.BLACK);
                }
                selectedAreaType = areaTypes[pos];
                applyFilters();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
//    private void setupAreaTypeSpinner() {
//        String[] areaTypes = {"All Areas", "Block", "District", "Assembly", "Division"};
//        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(
//                this, android.R.layout.simple_spinner_item, areaTypes);
//        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spAreaType.setAdapter(spinAdapter);
//
//        spAreaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                selectedAreaType = areaTypes[pos];
//                applyFilters();
//            }
//            @Override public void onNothingSelected(AdapterView<?> parent) {}
//        });
//    }

    // ══════════════════════════════════════════
    // DATE PICKERS
    // ══════════════════════════════════════════
    private void setupDatePickers() {
        etFromDate.setFocusable(false);
        etToDate.setFocusable(false);

        etFromDate.setOnClickListener(v -> showDatePicker(true));
        etToDate.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(boolean isFrom) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (dp, year, month, day) -> {
                    String date = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
                    if (isFrom) {
                        fromDate = date;
                        etFromDate.setText(String.format(Locale.US, "%02d/%02d/%04d", day, month + 1, year));
                    } else {
                        toDate = date;
                        etToDate.setText(String.format(Locale.US, "%02d/%02d/%04d", day, month + 1, year));
                    }
                    applyFilters();
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    // ══════════════════════════════════════════
    // AREA SEARCH
    // ══════════════════════════════════════════
    private void setupAreaSearch() {
        etAreaSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                areaQuery = s.toString().trim().toLowerCase(Locale.getDefault());
                applyFilters();
            }
        });
    }

    // ══════════════════════════════════════════
    // CLEAR FILTER
    // ══════════════════════════════════════════
    private void setupClearFilter() {
        btnClearFilter.setOnClickListener(v -> {
            fromDate = "";
            toDate = "";
            areaQuery = "";
            etFromDate.setText("");
            etToDate.setText("");
            etAreaSearch.setText("");
            spAreaType.setSelection(0);
            applyFilters();
        });
    }

    // ══════════════════════════════════════════
    // APPLY FILTERS  (Date + Area)
    // ══════════════════════════════════════════


    private void applyFilters() {
        filteredList.clear();

        for (MemberEntity m : allMembers) {
            boolean passDate = true;
            boolean passArea = true;

            // ── Date Filter ──
            if (!fromDate.isEmpty() || !toDate.isEmpty()) {
                String memberDate = "";
                if (m.createdDateTime != null && m.createdDateTime.length() >= 10) {
                    memberDate = m.createdDateTime.substring(0, 10);
                }
                if (!fromDate.isEmpty() && memberDate.compareTo(fromDate) < 0) passDate = false;
                if (!toDate.isEmpty()   && memberDate.compareTo(toDate)   > 0) passDate = false;
            }

            // ── Area + Name Filter ──
            if (!areaQuery.isEmpty()) {
                String fieldToCheck = getAreaField(m);
                passArea = fieldToCheck != null
                        && fieldToCheck.toLowerCase(Locale.getDefault()).contains(areaQuery);
            }

            if (passDate && passArea) filteredList.add(m);
        }

        adapter.notifyDataSetChanged();
        updateFilteredCount();
    }

    // ✅ YE METHOD UPDATE KARO
    private String getAreaField(MemberEntity m) {
        switch (selectedAreaType) {
            case "Block":    return safe(m.block);
            case "District": return safe(m.district);
            case "Assembly": return safe(m.assembly);
            case "Division": return safe(m.division);
            default:
                // ✅ "All Areas" — name + mobile + sabb area fields sabb check karo
                return safe(m.name) + " "
                        + safe(m.mobile) + " "
                        + safe(m.block) + " "
                        + safe(m.district) + " "
                        + safe(m.assembly) + " "
                        + safe(m.division) + " "
                        + safe(m.memberId);
        }
    }

//    private void applyFilters() {
//        filteredList.clear();
//
//        for (MemberEntity m : allMembers) {
//            boolean passDate = true;
//            boolean passArea = true;
//
//            // ── Date Filter ──
//            if (!fromDate.isEmpty() || !toDate.isEmpty()) {
//                // createdDateTime format: "yyyy-MM-dd HH:mm:ss"
//                String memberDate = m.createdDateTime != null
//                        ? m.createdDateTime.substring(0, 10)
//                        : "";
//
//                if (!fromDate.isEmpty() && memberDate.compareTo(fromDate) < 0) passDate = false;
//                if (!toDate.isEmpty()   && memberDate.compareTo(toDate)   > 0) passDate = false;
//            }
//
//            // ── Area Filter ──
//            if (!areaQuery.isEmpty()) {
//                String fieldToCheck = getAreaField(m);
//                passArea = fieldToCheck != null
//                        && fieldToCheck.toLowerCase(Locale.getDefault()).contains(areaQuery);
//            }
//
//            if (passDate && passArea) filteredList.add(m);
//        }
//
//        adapter.notifyDataSetChanged();
//        updateFilteredCount();
//    }

//    private String getAreaField(MemberEntity m) {
//        switch (selectedAreaType) {
//            case "Block":    return m.block;
//            case "District": return m.district;
//            case "Assembly": return m.assembly;
//            case "Division": return m.division;
//            default:
//                // "All Areas" — check all
//                return (safe(m.block) + " " + safe(m.district) + " "
//                        + safe(m.assembly) + " " + safe(m.division));
//        }
//    }

    private String safe(String s) { return s != null ? s : ""; }

    private void updateFilteredCount() {
        txtFilteredCount.setText(String.valueOf(filteredList.size()));
        boolean hasFilter = !fromDate.isEmpty() || !toDate.isEmpty() || !areaQuery.isEmpty();
        txtFilterLabel.setText(hasFilter ? "Filtered Results" : "All Members");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}