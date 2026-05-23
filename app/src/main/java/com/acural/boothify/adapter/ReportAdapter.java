package com.acural.boothify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acural.boothify.R;
import com.acural.boothify.model.MemberEntity;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    Context context;
    List<MemberEntity> list;

    public ReportAdapter(Context context, List<MemberEntity> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_report_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        MemberEntity m = list.get(position);

        h.txtSrNo.setText(String.valueOf(position + 1));
        h.txtName.setText(safe(m.name));
        h.txtMemberId.setText("ID: " + safe(m.memberId));
        h.txtMobile.setText("📱 " + safe(m.mobile));
        h.txtBlock.setText("📍 " + safe(m.block));
        h.txtDistrict.setText("🏛 " + safe(m.district));
        h.txtAssembly.setText("🗳 " + safe(m.assembly));

        // ✅ Date + Time dono show karo
        if (m.createdDateTime != null && m.createdDateTime.length() >= 10) {
            String date = m.createdDateTime.substring(0, 10);       // yyyy-MM-dd
            String time = m.createdDateTime.length() >= 19
                    ? m.createdDateTime.substring(11, 19)            // HH:mm:ss
                    : "";
            h.txtDateTime.setText("🕐 " + date + "  " + time);
        } else {
            h.txtDateTime.setText("🕐 ---");
        }

        // ✅ New / Existing badge
        if (m.isExistingMember == 1) {
            h.txtBadge.setText("Existing");
            h.txtBadge.setBackgroundResource(R.drawable.bg_badge_existing);
        } else {
            h.txtBadge.setText("New");
            h.txtBadge.setBackgroundResource(R.drawable.bg_badge_new);
        }
    }

    private String safe(String s) { return s != null ? s : "---"; }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSrNo, txtName, txtMemberId, txtMobile;
        TextView txtBlock, txtDistrict, txtAssembly, txtDateTime, txtBadge;

        ViewHolder(@NonNull View v) {
            super(v);
            txtSrNo      = v.findViewById(R.id.txtSrNo);
            txtName      = v.findViewById(R.id.txtReportName);
            txtMemberId  = v.findViewById(R.id.txtReportMemberId);
            txtMobile    = v.findViewById(R.id.txtReportMobile);
            txtBlock     = v.findViewById(R.id.txtReportBlock);
            txtDistrict  = v.findViewById(R.id.txtReportDistrict);
            txtAssembly  = v.findViewById(R.id.txtReportAssembly);
            txtDateTime  = v.findViewById(R.id.txtReportDateTime);
            txtBadge     = v.findViewById(R.id.txtReportBadge);
        }
    }
}