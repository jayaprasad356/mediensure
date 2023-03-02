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
import com.graymatter.mediensure.model.DentalData;
import com.graymatter.mediensure.model.OpdData;

import java.util.ArrayList;

public class PendingDentalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<DentalData> dentalData;
    Session session;


    public PendingDentalAdapter(Activity activity, ArrayList<DentalData> dentalData) {
        this.activity = activity;
        this.dentalData = dentalData;
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
        final DentalData dentalData1 = dentalData.get(position);

        holder.tvName.setText(dentalData1.getClinic_name());
        holder.tvMobile.setText(dentalData1.getMobile());
        holder.tvDateandTime.setText(dentalData1.getDatetime());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RadiologyEditActivity.class);
                intent.putExtra(Constant.ID,dentalData1.getId());
                intent.putExtra(Constant.CENTER_NAME,dentalData1.getClinic_name());
                intent.putExtra(Constant.EMAIL,dentalData1.getEmail());
                intent.putExtra(Constant.MOBILE,dentalData1.getMobile());
                intent.putExtra(Constant.DATETIME,dentalData1.getDatetime());
                intent.putExtra(Constant.LATITUDE,dentalData1.getLatitude());
                intent.putExtra(Constant.LONGITUDE,dentalData1.getLongitude());
                intent.putExtra(Constant.STATUS,dentalData1.getStatus());
                intent.putExtra(Constant.REMARKS,dentalData1.getRemarks());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dentalData.size();
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

