package com.acural.boothify.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import com.acural.boothify.R;
import com.acural.boothify.UiActivity.IdentityCardActivity;
import com.acural.boothify.model.MemberEntity;
import com.acural.boothify.roomdb.AppDatabase;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    Context context;
    List<MemberEntity> list;
    String screenType; // "viewdata" or "existingdata"

    // Constructor now accepts screenType
    public MemberAdapter(Context context, List<MemberEntity> list, String screenType) {
        this.context    = context;
        this.list       = list;
        this.screenType = screenType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberEntity member = list.get(position);

        holder.txtMemberId.setText("Member ID : " + member.memberId);
        holder.txtName.setText("Name : " + member.name);
        holder.txtBlock.setText("Block : " + member.block);

        if (member.partyRole == null || member.partyRole.isEmpty()) {
            holder.txtRole.setText("N/A");
        } else {
            holder.txtRole.setText(" " + member.partyRole);
        }

        if (member.imagePath != null && !member.imagePath.isEmpty()) {
            java.io.File file = new java.io.File(member.imagePath);
            if (file.exists()) {
                android.graphics.Bitmap bitmap =
                        android.graphics.BitmapFactory.decodeFile(member.imagePath);
                holder.img2.setImageBitmap(bitmap);
            } else {
                holder.img2.setImageResource(R.drawable.profile_placeholder);
            }
        } else {
            holder.img2.setImageResource(R.drawable.profile_placeholder);
        }


        // ── VIEWDATA mode ──────────────────────────────────────────────
        // Show: Update Role button only
        // Hide: Generate ID button
        // If role is empty → Toast warning, don't open dialog
        if ("viewdata".equals(screenType)) {

            holder.btnAssignRole.setText("Update Role");
            holder.btnAssignRole.setVisibility(View.VISIBLE);
            holder.btnGenerateId.setVisibility(View.VISIBLE);

            holder.btnAssignRole.setOnClickListener(v -> {
                if (member.partyRole == null || member.partyRole.isEmpty()) {
                    Toast.makeText(context,
                            "Please add role first",
                            Toast.LENGTH_SHORT).show();
                } else {
                    showRoleDialog(member, holder);
                }
            });
            holder.btnGenerateId.setOnClickListener(v ->
                    openIdentityCard(member)
            );
            // ── EXISTINGDATA mode ──────────────────────────────────────────
            // Show: Assign/Update Role button only
            // Hide: Generate ID button entirely
        } else if ("existingdata".equals(screenType)) {

            holder.btnAssignRole.setText("Assign Role");
            holder.btnAssignRole.setVisibility(View.VISIBLE);
            holder.btnGenerateId.setVisibility(View.GONE);

            holder.btnAssignRole.setOnClickListener(v ->
                    showRoleDialog(member, holder)
            );

            // ── DEFAULT (fallback) ─────────────────────────────────────────
        } else {
            holder.btnAssignRole.setVisibility(View.VISIBLE);
            holder.btnGenerateId.setVisibility(View.VISIBLE);

            holder.btnAssignRole.setOnClickListener(v ->
                    showRoleDialog(member, holder)
            );
            holder.btnGenerateId.setOnClickListener(v ->
                    openIdentityCard(member)
            );
        }
    }

    private void openIdentityCard(MemberEntity member) {
        Intent intent = new Intent(context, IdentityCardActivity.class);
        intent.putExtra("MEMBER_ID",      member.memberId);
        intent.putExtra("MEMBER_NAME",    member.name);
        intent.putExtra("MEMBER_BLOCK",   member.block);
        intent.putExtra("MEMBER_ROLE",    member.partyRole);
        intent.putExtra("MEMBER_FATHER",  member.father);
        intent.putExtra("MEMBER_PHONE",   member.mobile);
        intent.putExtra("MEMBER_VOTERID", member.voterId);
        intent.putExtra("MEMBER_IMAGE",   member.imagePath);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void showRoleDialog(MemberEntity member, ViewHolder holder) {
        Context ctx = context;

        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, 0, 0, 0);
        root.setBackgroundColor(Color.parseColor("#FFFFFF"));

        // ── Header ────────────────────────────────────────────────────
        LinearLayout header = new LinearLayout(ctx);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setBackgroundColor(Color.parseColor("#1E3A5F"));
        header.setGravity(Gravity.CENTER);
        int hp = dp(ctx, 20);
        header.setPadding(hp, hp, hp, hp);

        TextView icon = new TextView(ctx);
        icon.setText("🎖️");
        icon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
        icon.setGravity(Gravity.CENTER);

        TextView title = new TextView(ctx);
        title.setText("Assign Party Role");
        title.setTextColor(Color.parseColor("#FFD700"));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setLetterSpacing(0.05f);
        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        titleParams.topMargin = dp(ctx, 6);
        title.setLayoutParams(titleParams);

        TextView subtitle = new TextView(ctx);
        subtitle.setText(member.name);
        subtitle.setTextColor(Color.parseColor("#94A3B8"));
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        subtitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subParams =
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        subParams.topMargin = dp(ctx, 4);
        subtitle.setLayoutParams(subParams);

        header.addView(icon);
        header.addView(title, titleParams);
        header.addView(subtitle, subParams);

        // ── Gold divider ──────────────────────────────────────────────
        View divider = new View(ctx);
        divider.setBackgroundColor(Color.parseColor("#FFD700"));
        divider.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, dp(ctx, 3)));

        // ── Body ──────────────────────────────────────────────────────
        LinearLayout body = new LinearLayout(ctx);
        body.setOrientation(LinearLayout.VERTICAL);
        int bp = dp(ctx, 24);
        body.setPadding(bp, bp, bp, dp(ctx, 8));

        TextView chipLabel = new TextView(ctx);
        chipLabel.setText("QUICK SELECT");
        chipLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        chipLabel.setTextColor(Color.parseColor("#9CA3AF"));
        chipLabel.setTypeface(null, Typeface.BOLD);
        chipLabel.setLetterSpacing(0.1f);

        HorizontalScrollView chipScroll = new HorizontalScrollView(ctx);
        chipScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout.LayoutParams scrollParams =
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        scrollParams.topMargin = dp(ctx, 10);
        scrollParams.bottomMargin = dp(ctx, 16);
        chipScroll.setLayoutParams(scrollParams);

        LinearLayout chipRow = new LinearLayout(ctx);
        chipRow.setOrientation(LinearLayout.HORIZONTAL);
        chipRow.setGravity(Gravity.CENTER_VERTICAL);

        String[] roles = {
                "Adyaksh", "Jila Adyaksh", "Mantri", "Mahasachiv",
                "Sachiv", "Upadhyaksh", "Karyakarta", "Sadasya"
        };

        EditText editText = new EditText(ctx);
        editText.setHint("Type custom role...");
        editText.setHintTextColor(Color.parseColor("#9CA3AF"));
        editText.setTextColor(Color.parseColor("#1F2937"));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setTypeface(null, Typeface.BOLD);
        editText.setPadding(dp(ctx, 14), dp(ctx, 14), dp(ctx, 14), dp(ctx, 14));
        editText.setBackgroundResource(android.R.drawable.edit_text);

        if (member.partyRole != null && !member.partyRole.isEmpty()) {
            editText.setText(member.partyRole);
            editText.setSelection(member.partyRole.length());
        }

        for (String roleOption : roles) {
            TextView chip = new TextView(ctx);
            chip.setText(roleOption);
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            chip.setTextColor(Color.parseColor("#1E3A5F"));
            chip.setTypeface(null, Typeface.BOLD);
            chip.setPadding(dp(ctx, 14), dp(ctx, 8), dp(ctx, 14), dp(ctx, 8));

            GradientDrawable chipBg = new GradientDrawable();
            chipBg.setShape(GradientDrawable.RECTANGLE);
            chipBg.setCornerRadius(dp(ctx, 20));
            chipBg.setColor(Color.parseColor("#EFF6FF"));
            chipBg.setStroke(dp(ctx, 1), Color.parseColor("#BFDBFE"));
            chip.setBackground(chipBg);

            LinearLayout.LayoutParams chipParams =
                    new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            chipParams.rightMargin = dp(ctx, 8);
            chip.setLayoutParams(chipParams);

            chip.setOnClickListener(v -> {
                editText.setText(roleOption);
                editText.setSelection(roleOption.length());

                for (int i = 0; i < chipRow.getChildCount(); i++) {
                    View c = chipRow.getChildAt(i);
                    if (c instanceof TextView) {
                        GradientDrawable bg = new GradientDrawable();
                        bg.setShape(GradientDrawable.RECTANGLE);
                        bg.setCornerRadius(dp(ctx, 20));
                        if (c == chip) {
                            bg.setColor(Color.parseColor("#1E3A5F"));
                            bg.setStroke(dp(ctx, 1), Color.parseColor("#1E3A5F"));
                            ((TextView) c).setTextColor(Color.parseColor("#FFD700"));
                        } else {
                            bg.setColor(Color.parseColor("#EFF6FF"));
                            bg.setStroke(dp(ctx, 1), Color.parseColor("#BFDBFE"));
                            ((TextView) c).setTextColor(Color.parseColor("#1E3A5F"));
                        }
                        c.setBackground(bg);
                    }
                }
            });

            chipRow.addView(chip, chipParams);
        }

        chipScroll.addView(chipRow);

        TextView inputLabel = new TextView(ctx);
        inputLabel.setText("OR TYPE CUSTOM ROLE");
        inputLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        inputLabel.setTextColor(Color.parseColor("#9CA3AF"));
        inputLabel.setTypeface(null, Typeface.BOLD);
        inputLabel.setLetterSpacing(0.1f);
        LinearLayout.LayoutParams inputLabelParams =
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        inputLabelParams.bottomMargin = dp(ctx, 8);
        inputLabel.setLayoutParams(inputLabelParams);

        body.addView(chipLabel);
        body.addView(chipScroll, scrollParams);
        body.addView(inputLabel, inputLabelParams);
        body.addView(editText);

        // ── Button row ────────────────────────────────────────────────
        LinearLayout btnRow = new LinearLayout(ctx);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        btnRow.setGravity(Gravity.END);
        int brp = dp(ctx, 16);
        btnRow.setPadding(brp, dp(ctx, 8), brp, brp);

        Button btnCancel = new Button(ctx);
        btnCancel.setText("Cancel");
        btnCancel.setTextColor(Color.parseColor("#6B7280"));
        btnCancel.setAllCaps(false);
        btnCancel.setBackground(null);
        btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btnCancel.setTypeface(null, Typeface.BOLD);

        Button btnSave = new Button(ctx);
        btnSave.setText("Save Role");
        btnSave.setTextColor(Color.parseColor("#FFFFFF"));
        btnSave.setAllCaps(false);
        btnSave.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btnSave.setTypeface(null, Typeface.BOLD);
        GradientDrawable saveBg = new GradientDrawable();
        saveBg.setShape(GradientDrawable.RECTANGLE);
        saveBg.setCornerRadius(dp(ctx, 8));
        saveBg.setColor(Color.parseColor("#1E3A5F"));
        btnSave.setBackground(saveBg);
        LinearLayout.LayoutParams saveParams =
                new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        btnSave.setLayoutParams(saveParams);
        btnSave.setPadding(dp(ctx, 20), dp(ctx, 4), dp(ctx, 20), dp(ctx, 4));

        btnRow.addView(btnCancel);
        btnRow.addView(btnSave, saveParams);

        root.addView(header);
        root.addView(divider);
        root.addView(body);
        root.addView(btnRow);

        // ── Build & show dialog ───────────────────────────────────────
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setView(root)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    (int) (ctx.getResources().getDisplayMetrics().widthPixels * 0.92),
                    WRAP_CONTENT
            );
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String role = editText.getText().toString().trim();
            if (role.isEmpty()) {
                editText.setError("Please enter or select a role");
                return;
            }

            member.partyRole = role;

            // Save to Room database on a background thread
            new Thread(() -> {
                AppDatabase.getInstance(ctx).memberDao().update(member);
            }).start();

            // Update the UI label immediately
            holder.txtRole.setText(role);
            dialog.dismiss();
        });

        dialog.show();
    }

    private int dp(Context ctx, int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value,
                ctx.getResources().getDisplayMetrics()
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMemberId, txtName, txtBlock, txtRole;
        Button   btnAssignRole, btnGenerateId;
        ImageView img2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMemberId   = itemView.findViewById(R.id.txtMemberId);
            txtName       = itemView.findViewById(R.id.txtName);
            txtBlock      = itemView.findViewById(R.id.txtBlock);
            txtRole       = itemView.findViewById(R.id.txtRole);
            btnAssignRole = itemView.findViewById(R.id.btnAssignRole);
            btnGenerateId = itemView.findViewById(R.id.btnGenerateId);
            img2 = itemView.findViewById(R.id.itemIamge);
        }
    }
}
