package com.acural.boothify.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.acural.boothify.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FirstStep_Fragment extends Fragment {

    private EditText etName,etSurname, etMobile, etFather, etDob, etAge, etBlock;
    private EditText etDivision, etDistrict, etAssembly;
    private Button btnVerify, btnNext;
    private LinearLayout btnMale, btnFemale, btnOther;

    private boolean isExistingMember = false;
    private String selectedGender = "";
    private boolean isMobileVerified = false;

    private static final String PREF_NAME = "BoothifyPrefs";

    private static class AreaInfo {
        String division, district, assembly;

        AreaInfo(String d, String di, String a) {
            division = d;
            district = di;
            assembly = a;
        }
    }

    private static final Map<String, AreaInfo> BLOCK_MAP = new HashMap<>();

    static {
        String[] bina = {"Bina", "Khimlasa", "Bina City"};
        for (String b : bina)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Sagar", "Bina"));

        String[] khurai = {"Khurai Rural", "Malthone", "Khurai", "Bandri"};
        for (String b : khurai)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Sagar", "Khurai"));

        String[] surkhi = {"Jaisinhnagar", "Rahatgarh", "Surkhi", "Baleta"};
        for (String b : surkhi)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Sagar", "Surkhi"));

        String[] deori = {"Deori", "Kesli", "Gaurjhamar", "Maharajpur"};
        for (String b : deori)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Sagar", "Deori"));

        String[] rehli = {"Rehli", "Garhakota", "Shahpur", "Baleh", "Dhana"};
        for (String b : rehli)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Sagar", "Rehli"));

        String[] naryawali = {"Sagar Rural", "Naryawali", "Makronia", "Sagar Cantt"};
        for (String b : naryawali)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Sagar", "Naryawali"));

        String[] sagarCity = {"Sagar Civil Line", "Civil Line", "Katra", "Bada Bazar"};
        for (String b : sagarCity)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Sagar", "Sagar City"));

        String[] banda = {"Shahgarh", "Banda", "Dhamoni"};
        for (String b : banda)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Sagar", "Banda"));

        // Tikamgarh District
        String[] tikamgarh = {"Tikamgarh", "Badagaon", "Mawai"};
        for (String b : tikamgarh)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Tikamgarh", "Tikamgarh"));

        String[] jatara = {"Jatara", "Lidhora", "Chandera", "Bamhorikala"};
        for (String b : jatara)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Tikamgarh", "Jatara"));

        String[] khargapur = {"Palera", "Deri", "Khargapur", "Baldeogarh"};
        for (String b : khargapur)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Tikamgarh", "Khargapur"));

        // Niwari District
        String[] prithvipur = {"Prithvipur", "Jeron", "Digoda", "Mohangarh"};
        for (String b : prithvipur)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Niwari", "Prithvipur"));

        String[] niwari = {"Niwari", "Orchha", "Tarichar"};
        for (String b : niwari)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Niwari", "Niwari"));

        // Chhatarpur District
        String[] maharajpur = {"Nowgong", "Maharajpur", "Lugasi", "Harpalpur"};
        for (String b : maharajpur)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Chhatarpur", "Maharajpur"));

        String[] chandla = {"Chandla", "Gaurihar", "Barigarh", "Bachhon", "Sarwai"};
        for (String b : chandla)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Chhatarpur", "Chandla"));

        String[] rajnagar = {"Rajnagar", "Lavkush Nagar", "Chand Nagar", "Vikrampur"};
        for (String b : rajnagar)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Chhatarpur", "Rajnagar"));

        String[] chhatarpur = {"Mahewa", "Padariya", "East Chhatarpur", "West Chhatarpur", "Central Chhatarpur"};
        for (String b : chhatarpur)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Chhatarpur", "Chhatarpur"));

        String[] bijawar = {"Bijawar", "Matguwan", "Ishanagar", "Kishangarh", "Satai"};
        for (String b : bijawar)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Chhatarpur", "Bijawar"));

        String[] badaMalhara = {"Bada Malhara", "Bakswaha", "Guhara"};
        for (String b : badaMalhara)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Chhatarpur", "Bada Malhara"));

        // Damoh District
        String[] pathariya = {"Pathariya", "Batiyagarh", "Narsinghgarh"};
        for (String b : pathariya)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Damoh", "Pathariya"));

        String[] damoh = {"Damoh Rural", "Imliya Ghat", "Bandakpur"};
        for (String b : damoh)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Damoh", "Damoh"));

        String[] jabera = {"Jabera", "Tendukheda", "Nohta", "Tejgarh"};
        for (String b : jabera)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Damoh", "Jabera"));

        String[] hatta = {"Hatta", "Patera", "Hindoriya", "Gaisabad"};
        for (String b : hatta)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Damoh", "Hatta"));

        // Panna District
        String[] pawai = {"Shahnagar", "Raipura", "Pawai", "Simariya"};
        for (String b : pawai)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Panna", "Pawai"));

        String[] gunnaur = {"Amanganj", "Devendranagar"};
        for (String b : gunnaur)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Panna", "Gunnaur"));

        String[] panna = {"Ajaigarh", "Dharampur", "Panna"};
        for (String b : panna)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("SAGAR", "Panna", "Panna"));

        // ── REWA Division ──

        // Satna District
        String[] chitrakoot = {"Baroundha", "Majhgawan", "Virsinghpur"};
        for (String b : chitrakoot)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Satna", "Chitrakoot"));

        String[] raigaon = {"Singhpur", "Sohawal", "Kothi"};
        for (String b : raigaon)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Satna", "Raigaon"));

        String[] satna = {"Babupur", "Dhawari", "Satna City Block", "Nai Basti"};
        for (String b : satna)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Satna", "Satna"));

        String[] nagod = {"Nagod", "Parsmania", "Unchehra"};
        for (String b : nagod)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Satna", "Nagod"));

        String[] rampurBaghelan = {"Rampur Baghelan", "Baila", "Chaurhata", "Kotar"};
        for (String b : rampurBaghelan)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Satna", "Rampur Baghelan"));

        // Maihar District
        String[] maihar = {"Maihar", "Amdara", "Badera", "Nadan"};
        for (String b : maihar)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Maihar", "Maihar"));

        String[] amarpatan = {"Ramnagar", "Amarpatan", "Tala"};
        for (String b : amarpatan)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Maihar", "Amarpatan"));

        // Rewa District
        String[] sirmour = {"Sirmour", "Jawa", "Dabhoura"};
        for (String b : sirmour)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Rewa", "Sirmour"));

        String[] teonthar = {"Teonthar", "Garhi", "Chakghat"};
        for (String b : teonthar)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Rewa", "Teonthar"));

        String[] mangawan = {"Gangew"};
        for (String b : mangawan)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Rewa", "Mangawan"));

        String[] gurh = {"Gurh", "Govindgarh", "Rampur Karchuliyan"};
        for (String b : gurh) BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Rewa", "Gurh"));

        String[] rewa = {"Chirhula", "Dhekha", "Bodabagh"};
        for (String b : rewa) BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Rewa", "Rewa"));

        // Sidhi District
        String[] churhat = {"Rampur Naikin", "Churhat", "Hanumangarh"};
        for (String b : churhat)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Sidhi", "Churhat"));

        String[] sidhi = {"Sidhi City", "Semariya", "Kuchwahi", "Barambaba"};
        for (String b : sidhi)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Sidhi", "Sidhi"));

        String[] sihawal = {"Sihawal", "Bahri"};
        for (String b : sihawal)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Sidhi", "Sihawal"));

        String[] dhauhani = {"Majhauli", "Kusmi", "Madwas", "Mahuagaon"};
        for (String b : dhauhani)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Sidhi", "Dhauhani"));

        // Mauganj District
        String[] mauganj = {"Mauganj", "Khatkhari", "Hanumana"};
        for (String b : mauganj)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Mauganj", "Mauganj"));

        String[] deotalab = {"Naigarhi", "Deotalab", "Ragurajgarh"};
        for (String b : deotalab)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Mauganj", "Deotalab"));

        String[] chitrangi = {"Chitrangi", "Bairadah", "Karela", "Bagdara"};
        for (String b : chitrangi)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Mauganj", "Chitrangi"));

        // Singrauli District
        String[] singrauli = {"Baidhan", "Nava Nagar", "Shasan", "Hirwah", "Morwa"};
        for (String b : singrauli)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Singrauli", "Singrauli"));

        String[] devsar = {"Devsar", "Sarai", "Bargawan", "Langhadol", "Khutar", "Manda"};
        for (String b : devsar)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Singrauli", "Devsar"));

        // Semariya
        String[] semariya = {"Semariya", "Shahpur", "Bankuiya", "Majhiyar"};
        for (String b : semariya)
            BLOCK_MAP.put(b.toLowerCase(), new AreaInfo("REWA", "Rewa", "Semariya"));
    }

    // ─────────────────────────────────────────
    public FirstStep_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_step_, container, false);

        if (getArguments() != null) {
            isExistingMember = getArguments().getBoolean("isExistingMember", false);
        }

        initViews(view);
        setupMobileInput();
        setupOtpVerify();
        setupGenderSelection();
        setupDatePicker();
        setupBlockAutofill();
        setupNextButton();
        return view;
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etMobile = view.findViewById(R.id.etMobile);
        etFather = view.findViewById(R.id.etFather);
        etDob = view.findViewById(R.id.etDob);
        etAge = view.findViewById(R.id.etAge);
        etBlock = view.findViewById(R.id.etBlock);
        etDivision = view.findViewById(R.id.etDivision);
        etDistrict = view.findViewById(R.id.etDistrict);
        etAssembly = view.findViewById(R.id.etAssembly);
        btnVerify = view.findViewById(R.id.btnVerify);
        btnNext = view.findViewById(R.id.btnNext);
        btnMale = view.findViewById(R.id.btnMale);
        btnFemale = view.findViewById(R.id.btnFemale);
        btnOther = view.findViewById(R.id.btnOther);
        etSurname = view.findViewById(R.id.etSurname);
    }

    // ── Mobile: max 10 digits ──────────────────
    private void setupMobileInput() {
        etMobile.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        etMobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
    }

    // ── OTP Verify (hardcoded 1234) ────────────
    private void setupOtpVerify() {
        btnVerify.setOnClickListener(v -> {
            String mobile = etMobile.getText().toString().trim();
            if (mobile.length() != 10) {
                Toast.makeText(getContext(), "Enter valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            // Show OTP dialog
            showOtpDialog(mobile);
        });
    }

    private void showOtpDialog(String mobile) {

        // ─────────────────────────────────────────────
        // MAIN CONTAINER
        // ─────────────────────────────────────────────
        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(50, 50, 50, 30);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(android.graphics.Color.WHITE);
        bg.setCornerRadius(40f);
        root.setBackground(bg);


        ImageView icon = new ImageView(requireContext());

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(140, 140);

        iconParams.gravity = Gravity.CENTER_HORIZONTAL;

        icon.setLayoutParams(iconParams);

        icon.setImageResource(R.drawable.call);

        GradientDrawable iconBg = new GradientDrawable();
        iconBg.setShape(GradientDrawable.OVAL);
        iconBg.setColor(android.graphics.Color.parseColor("#E8F0FE"));

        icon.setBackground(iconBg);

        icon.setPadding(30, 30, 30, 30);

        root.addView(icon);


        // ─────────────────────────────────────────────
        // TITLE
        // ─────────────────────────────────────────────
        TextView title = new TextView(requireContext());

        title.setText("OTP Verification");

        title.setTextSize(22);

        title.setTypeface(null, Typeface.BOLD);

        title.setTextColor(android.graphics.Color.parseColor("#111827"));

        title.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        titleParams.topMargin = 25;

        title.setLayoutParams(titleParams);

        root.addView(title);


        // ─────────────────────────────────────────────
        // SUBTITLE
        // ─────────────────────────────────────────────
        TextView subtitle = new TextView(requireContext());

        subtitle.setText("Enter the 4-digit OTP sent to\n+91-" + mobile);

        subtitle.setTextSize(14);

        subtitle.setGravity(Gravity.CENTER);

        subtitle.setTextColor(android.graphics.Color.parseColor("#6B7280"));

        LinearLayout.LayoutParams subParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        subParams.topMargin = 12;

        subtitle.setLayoutParams(subParams);

        root.addView(subtitle);


        // ─────────────────────────────────────────────
        // OTP BOX CONTAINER
        // ─────────────────────────────────────────────
        LinearLayout otpContainer = new LinearLayout(requireContext());

        otpContainer.setOrientation(LinearLayout.HORIZONTAL);

        otpContainer.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams otpContainerParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        otpContainerParams.topMargin = 35;

        otpContainer.setLayoutParams(otpContainerParams);


        // ─────────────────────────────────────────────
        // OTP BOXES
        // ─────────────────────────────────────────────
        EditText otp1 = createOtpBox();
        EditText otp2 = createOtpBox();
        EditText otp3 = createOtpBox();
        EditText otp4 = createOtpBox();

        otpContainer.addView(otp1);
        otpContainer.addView(otp2);
        otpContainer.addView(otp3);
        otpContainer.addView(otp4);

        root.addView(otpContainer);


        // ─────────────────────────────────────────────
        // ERROR TEXT
        // ─────────────────────────────────────────────
        TextView errorText = new TextView(requireContext());

        errorText.setText("Invalid OTP");

        errorText.setTextColor(android.graphics.Color.parseColor("#EF4444"));

        errorText.setTextSize(12);

        errorText.setGravity(Gravity.CENTER);

        errorText.setVisibility(View.GONE);

        LinearLayout.LayoutParams errorParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        errorParams.topMargin = 15;

        errorText.setLayoutParams(errorParams);

        root.addView(errorText);


        // ─────────────────────────────────────────────
        // VERIFY BUTTON
        // ─────────────────────────────────────────────
        Button verifyBtn = new Button(requireContext());

        verifyBtn.setText("Verify OTP");

        verifyBtn.setTextSize(16);

        verifyBtn.setTypeface(null, Typeface.BOLD);

        verifyBtn.setTextColor(android.graphics.Color.WHITE);

        verifyBtn.setAllCaps(false);

        verifyBtn.setEnabled(false);

        verifyBtn.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        android.graphics.Color.parseColor("#9CA3AF")
                )
        );

        LinearLayout.LayoutParams verifyParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        120
                );

        verifyParams.topMargin = 30;

        verifyBtn.setLayoutParams(verifyParams);

        root.addView(verifyBtn);


        // ─────────────────────────────────────────────
        // CREATE DIALOG
        // ─────────────────────────────────────────────
        android.app.AlertDialog dialog =
                new android.app.AlertDialog.Builder(requireContext())
                        .setView(root)
                        .create();

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT)
        );

        dialog.setCancelable(true);


        // ─────────────────────────────────────────────
        // OTP AUTO MOVE FUNCTION
        // ─────────────────────────────────────────────
        setupOtpBox(otp1, null, otp2, verifyBtn,
                otp1, otp2, otp3, otp4);

        setupOtpBox(otp2, otp1, otp3, verifyBtn,
                otp1, otp2, otp3, otp4);

        setupOtpBox(otp3, otp2, otp4, verifyBtn,
                otp1, otp2, otp3, otp4);

        setupOtpBox(otp4, otp3, null, verifyBtn,
                otp1, otp2, otp3, otp4);


        // ─────────────────────────────────────────────
        // VERIFY BUTTON CLICK
        // ─────────────────────────────────────────────
        verifyBtn.setOnClickListener(v -> {

            String otp =
                    otp1.getText().toString().trim() +
                            otp2.getText().toString().trim() +
                            otp3.getText().toString().trim() +
                            otp4.getText().toString().trim();

            if (otp.equals("1234")) {

                isMobileVerified = true;

                btnVerify.setText("✓ Verified");

                btnVerify.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(
                                android.graphics.Color.parseColor("#22C55E")
                        )
                );

                btnVerify.setEnabled(false);

                Toast.makeText(getContext(),
                        "Mobile verified successfully!",
                        Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            } else {

                errorText.setVisibility(View.VISIBLE);

                otp1.setText("");
                otp2.setText("");
                otp3.setText("");
                otp4.setText("");

                otp1.requestFocus();
            }
        });

        dialog.show();
    }

    private EditText createOtpBox() {

        EditText et = new EditText(requireContext());

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(130, 130);

        params.setMargins(10, 0, 10, 0);

        et.setLayoutParams(params);

        et.setGravity(Gravity.CENTER);

        et.setTextSize(24);

        et.setTypeface(null, Typeface.BOLD);

        et.setInputType(InputType.TYPE_CLASS_NUMBER);

        et.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(1)
        });

        et.setTextColor(android.graphics.Color.parseColor("#111827"));

        GradientDrawable bg = new GradientDrawable();

        bg.setCornerRadius(24f);

        bg.setStroke(3,
                android.graphics.Color.parseColor("#D1D5DB"));

        bg.setColor(android.graphics.Color.WHITE);

        et.setBackground(bg);

        return et;
    }

    private void setupOtpBox(EditText current,
                             EditText previous,
                             EditText next,
                             Button verifyBtn,
                             EditText otp1,
                             EditText otp2,
                             EditText otp3,
                             EditText otp4) {

        current.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {

                    if (next != null) {

                        next.requestFocus();
                    }
                } else if (s.length() == 0) {

                    if (previous != null) {

                        previous.requestFocus();
                    }
                }

                String otp =
                        otp1.getText().toString().trim() +
                                otp2.getText().toString().trim() +
                                otp3.getText().toString().trim() +
                                otp4.getText().toString().trim();

                boolean complete = otp.length() == 4;

                verifyBtn.setEnabled(complete);

                if (complete) {

                    verifyBtn.setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(
                                    android.graphics.Color.parseColor("#0047BB")
                            )
                    );

                } else {

                    verifyBtn.setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(
                                    android.graphics.Color.parseColor("#9CA3AF")
                            )
                    );
                }
            }
        });
    }

    private void setupGenderSelection() {
        btnMale.setOnClickListener(v -> selectGender("Male", btnMale, btnFemale, btnOther));
        btnFemale.setOnClickListener(v -> selectGender("Female", btnFemale, btnMale, btnOther));
        btnOther.setOnClickListener(v -> selectGender("Other", btnOther, btnMale, btnFemale));
    }

    private void selectGender(String gender, LinearLayout selected,
                              LinearLayout other1, LinearLayout other2) {
        selectedGender = gender;
        selected.setBackgroundResource(R.drawable.bg_gender_selected);
        other1.setBackgroundResource(R.drawable.bg_gender_unselected);
        other2.setBackgroundResource(R.drawable.bg_gender_unselected);

        // Update text colors
        updateGenderTextColor(selected, "#0047BB");
        updateGenderTextColor(other1, "#111827");
        updateGenderTextColor(other2, "#111827");
    }

    private void updateGenderTextColor(LinearLayout layout, String colorHex) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(android.graphics.Color.parseColor(colorHex));
            }
        }
    }

    // ── Date Picker & Age Calculation ──────────
    private void setupDatePicker() {
        etDob.setFocusable(false);
        etDob.setClickable(true);
        etDob.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();

        // Max date = today minus 18 years
        Calendar maxCal = Calendar.getInstance();
        maxCal.add(Calendar.YEAR, -18);

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (datePicker, year, month, day) -> {
                    String dob = String.format("%02d/%02d/%04d", day, month + 1, year);
                    etDob.setText(dob);
                    calculateAge(year, month, day);
                },
                maxCal.get(Calendar.YEAR),
                maxCal.get(Calendar.MONTH),
                maxCal.get(Calendar.DAY_OF_MONTH)
        );

        // Restrict: cannot select a date that would make age < 18
        dialog.getDatePicker().setMaxDate(maxCal.getTimeInMillis());
        dialog.show();
    }

    private void calculateAge(int birthYear, int birthMonth, int birthDay) {
        Calendar dob = Calendar.getInstance();
        dob.set(birthYear, birthMonth, birthDay);

        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) age--;

        etAge.setText(String.valueOf(age));
    }

    // ── Block → Auto-fill Division/District/Assembly ──
    private void setupBlockAutofill() {
        etBlock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim().toLowerCase();
                if (input.isEmpty()) {
                    etDivision.setText("");
                    etDistrict.setText("");
                    etAssembly.setText("");
                    return;
                }

                AreaInfo info = null;

                // Exact match first
                if (BLOCK_MAP.containsKey(input)) {
                    info = BLOCK_MAP.get(input);
                } else {
                    // Partial/contains match
                    for (Map.Entry<String, AreaInfo> entry : BLOCK_MAP.entrySet()) {
                        if (entry.getKey().contains(input) || input.contains(entry.getKey())) {
                            info = entry.getValue();
                            break;
                        }
                    }
                }

                if (info != null) {
                    etDivision.setText(info.division);
                    etDistrict.setText(info.district);
                    etAssembly.setText(info.assembly);
                } else {
                    etDivision.setText("");
                    etDistrict.setText("");
                    etAssembly.setText("");
                }
            }
        });
    }


    private void setupNextButton() {
        btnNext.setOnClickListener(v -> {
            if (!validateFields()) return;
            saveToSharedPrefs();

            SecondStep_Fragment secondFragment = new SecondStep_Fragment();
            Bundle args = new Bundle();
            args.putBoolean("isExistingMember", isExistingMember);
            secondFragment.setArguments(args);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameContainer, secondFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private boolean validateFields() {
        String name = etName.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String father = etFather.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String block = etBlock.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Full name is required");
            etName.requestFocus();
            return false;
        }
        if (surname.isEmpty()) {
            etSurname.setError("Surname is required");
            etSurname.requestFocus();
            return false;
        }
        if (mobile.length() != 10) {
            etMobile.setError("Enter valid 10-digit mobile number");
            etMobile.requestFocus();
            return false;
        }
        if (!isMobileVerified) {
            Toast.makeText(getContext(), "Please verify your mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (father.isEmpty()) {
            etFather.setError("Father/Husband name is required");
            etFather.requestFocus();
            return false;
        }
        if (selectedGender.isEmpty()) {
            Toast.makeText(getContext(), "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dob.isEmpty()) {
            Toast.makeText(getContext(), "Please select date of birth", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (block.isEmpty()) {
            etBlock.setError("Block is required");
            etBlock.requestFocus();
            return false;
        }
        return true;
    }

    private void saveToSharedPrefs() {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString("name", etName.getText().toString().trim())
                .putString("surname", etSurname.getText().toString().trim())
                .putString("mobile", etMobile.getText().toString().trim())
                .putString("father", etFather.getText().toString().trim())
                .putString("gender", selectedGender)
                .putString("dob", etDob.getText().toString().trim())
                .putString("age", etAge.getText().toString().trim())
                .putString("block", etBlock.getText().toString().trim())
                .putString("division", etDivision.getText().toString().trim())
                .putString("district", etDistrict.getText().toString().trim())
                .putString("assembly", etAssembly.getText().toString().trim())
                .putBoolean("isExistingMember", isExistingMember)
                .apply();
    }


}

