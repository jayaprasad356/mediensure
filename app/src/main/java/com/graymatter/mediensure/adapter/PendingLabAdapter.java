package com.graymatter.mediensure.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graymatter.mediensure.R;
import com.graymatter.mediensure.RadiologyEditActivity;
import com.graymatter.mediensure.helper.Constant;
import com.graymatter.mediensure.helper.Session;
import com.graymatter.mediensure.model.LabData;
import com.graymatter.mediensure.model.RadiologyData;

import java.util.ArrayList;

public class PendingLabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<LabData> labData;
    Session session;
    String Totalprice = "";


    public PendingLabAdapter(Activity activity, ArrayList<LabData> labData) {
        this.activity = activity;
        this.labData = labData;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.radiology_lyt, parent, false);
        return new ExploreItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
        session = new Session(activity);
        final ExploreItemHolder holder = (ExploreItemHolder) holderParent;
        final LabData labData1 = labData.get(position);

        holder.tvName.setText(labData1.getCenter_name());
        holder.tvMobile.setText(labData1.getMobile());
        holder.tvDateandTime.setText(labData1.getDatetime());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RadiologyEditActivity.class);
                intent.putExtra(Constant.ID,labData1.getId());
                intent.putExtra(Constant.CENTER_NAME,labData1.center_name);
                intent.putExtra(Constant.EMAIL,labData1.getEmail());
                intent.putExtra(Constant.MOBILE,labData1.getMobile());
                intent.putExtra(Constant.MANAGER_NAME,labData1.getManager_name());
                intent.putExtra(Constant.CENTER_ADDRESS,labData1.getCenter_address());
                intent.putExtra(Constant.OPERATIONAL_HOURS,labData1.getOperational_hours());
                intent.putExtra(Constant.IMAGE,labData1.getImage());
                intent.putExtra(Constant.DATETIME,labData1.getDatetime());
                intent.putExtra(Constant.LATITUDE,labData1.getLatitude());
                intent.putExtra(Constant.LONGITUDE,labData1.getLongitude());
                intent.putExtra(Constant.STATUS,labData1.getStatus());
                intent.putExtra(Constant.REMARKS,labData1.getRemarks());
               activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return labData.size();
    }

    static class ExploreItemHolder extends RecyclerView.ViewHolder {

        final TextView tvName,tvMobile, tvDateandTime,btnEdit;
        public ExploreItemHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvDateandTime = itemView.findViewById(R.id.tvDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);


        }
    }
}

