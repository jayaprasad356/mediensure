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
import com.graymatter.mediensure.model.PharmacyData;
import com.graymatter.mediensure.model.RadiologyData;

import java.util.ArrayList;

public class PendingPharmacyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<PharmacyData> pharmacyDataArrayList;
    Session session;


    public PendingPharmacyAdapter(Activity activity, ArrayList<PharmacyData> pharmacyDataArrayList) {
        this.activity = activity;
        this.pharmacyDataArrayList = pharmacyDataArrayList;
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
        final PharmacyData radiology = pharmacyDataArrayList.get(position);

        holder.tvName.setText(radiology.getShop_name());
        holder.tvMobile.setText(radiology.getMobile());
        holder.tvDateandTime.setText(radiology.getDatetime());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RadiologyEditActivity.class);
                intent.putExtra(Constant.ID,radiology.getId());
                intent.putExtra(Constant.CENTER_NAME,radiology.getShop_name());
                intent.putExtra(Constant.EMAIL,radiology.getEmail());
                intent.putExtra(Constant.MOBILE,radiology.getMobile());
                intent.putExtra(Constant.DATETIME,radiology.getDatetime());
                intent.putExtra(Constant.LATITUDE,radiology.getLatitude());
                intent.putExtra(Constant.LONGITUDE,radiology.getLongitude());
                intent.putExtra(Constant.STATUS,radiology.getStatus());
                intent.putExtra(Constant.REMARKS,radiology.getRemarks());
               activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pharmacyDataArrayList.size();
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

