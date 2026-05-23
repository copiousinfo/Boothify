package com.acural.boothify.UiActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.acural.boothify.R;

import java.io.File;
import java.io.FileOutputStream;

public class IdentityCardActivity extends AppCompatActivity {

    TextView txtCardMemberId;
    TextView txtCardMemberIdDetail;
    TextView txtCardName;
    TextView txtCardMobile;
    TextView txtCardGuardian;
    TextView txtCardVoterId;

    Button btnShareCard;
    Button btnBack;

    ImageView img1;
    View cardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_card);

        // Link Views
        txtCardMemberId       = findViewById(R.id.txtCardMemberId);
        txtCardMemberIdDetail = findViewById(R.id.txtCardMemberIdDetail);
        txtCardName           = findViewById(R.id.txtCardName);
        txtCardMobile         = findViewById(R.id.txtCardMobile);
        img1=findViewById(R.id.profileImage);
        txtCardGuardian       = findViewById(R.id.txtCardGuardian);
        txtCardVoterId        = findViewById(R.id.txtCardVoterId);

        btnShareCard  = findViewById(R.id.btnShareCard);
        btnBack       = findViewById(R.id.btnBack);
        cardContainer = findViewById(R.id.cardContainer);

        String memberId  = getIntent().getStringExtra("MEMBER_ID");
        String name      = getIntent().getStringExtra("MEMBER_NAME");
        String mobile    = getIntent().getStringExtra("MEMBER_PHONE");
        String guardian  = getIntent().getStringExtra("MEMBER_FATHER");
        String voterId   = getIntent().getStringExtra("MEMBER_VOTERID");
        String block     = getIntent().getStringExtra("MEMBER_BLOCK");
        String role      = getIntent().getStringExtra("MEMBER_ROLE");
        String imagePath = getIntent().getStringExtra("MEMBER_IMAGE");
        memberId = notEmpty(memberId);
        name     = notEmpty(name);
        mobile   = notEmpty(mobile);
        guardian = notEmpty(guardian);
        voterId  = notEmpty(voterId);
        block    = notEmpty(block);
        role     = notEmpty(role);

        if (imagePath != null && !imagePath.isEmpty()) {
            java.io.File file = new java.io.File(imagePath);
            if (file.exists()) {
                android.graphics.Bitmap bitmap =
                        android.graphics.BitmapFactory.decodeFile(imagePath);
                img1.setImageBitmap(bitmap);
            }
        } else {
            img1.setImageResource(R.drawable.profile_placeholder);
        }


        txtCardMemberId.setText(memberId);
        txtCardMemberIdDetail.setText(memberId);
        txtCardName.setText(name);
        txtCardMobile.setText(mobile);
        txtCardGuardian.setText(guardian);
        txtCardVoterId.setText(voterId);;

        btnBack.setOnClickListener(v -> finish());

        final String finalMemberId  = memberId;
        final String finalName      = name;
        final String finalMobile    = mobile;
        final String finalGuardian  = guardian;
        final String finalVoterId   = voterId;
        final String finalBlock     = block;
        final String finalRole      = role;

        btnShareCard.setOnClickListener(v -> {

            Bitmap cardBitmap = captureViewAsBitmap(cardContainer);
            if (cardBitmap == null) {
                Toast.makeText(this, "Could not capture card", Toast.LENGTH_SHORT).show();
                return;
            }

            File shareFile = saveBitmapToCache(cardBitmap);
            if (shareFile == null) {
                Toast.makeText(this, "Could not save card image", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri fileUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    shareFile
            );

            String shareText =
                    "Member Identity Card\n" +
                            "──────────────────\n" +
                            "Member ID : " + finalMemberId  + "\n" +
                            "Name      : " + finalName      + "\n" +
                            "Mobile    : " + finalMobile    + "\n" +
                            "Father    : " + finalGuardian  + "\n" +
                            "Block     : " + finalBlock     + "\n" +
//                            "Role      : " + finalRole      + "\n" +
                            "Voter ID  : " + finalVoterId;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Share Identity Card via"));
        });
    }

    private Bitmap captureViewAsBitmap(View view) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(
                    view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private File saveBitmapToCache(Bitmap bitmap) {
        try {
            File dir = new File(getCacheDir(), "shared_images");
            dir.mkdirs();
            File file = new File(dir, "identity_card.png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String notEmpty(String s) {
        return (s == null || s.trim().isEmpty()) ? "---" : s.trim();
    }
}