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
import com.graymatter.mediensure.model.RadiologyData;

import java.util.ArrayList;

public class PendingRadiologyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<RadiologyData> radiologyDataArrayList;
    Session session;
    String Totalprice = "";


    public PendingRadiologyAdapter(Activity activity, ArrayList<RadiologyData> radiologyData) {
        this.activity = activity;
        this.radiologyDataArrayList = radiologyData;
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
        final RadiologyData radiology = radiologyDataArrayList.get(position);

        holder.tvName.setText(radiology.getCenter_name());
        holder.tvMobile.setText(radiology.getMobile());
        holder.tvDateandTime.setText(radiology.getDatetime());
        if(radiology.getStatus().equals("1")){
            holder.btnEdit.setVisibility(View.GONE);
        }
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RadiologyEditActivity.class);
                intent.putExtra(Constant.ID,radiology.getId());
                intent.putExtra(Constant.CENTER_NAME,radiology.center_name);
                intent.putExtra(Constant.EMAIL,radiology.getEmail());
                intent.putExtra(Constant.MOBILE,radiology.getMobile());
                intent.putExtra(Constant.MANAGER_NAME,radiology.getManager_name());
                intent.putExtra(Constant.CENTER_ADDRESS,radiology.getCenter_address());
                intent.putExtra(Constant.OPERATIONAL_HOURS,radiology.getOperational_hours());
                intent.putExtra(Constant.IMAGE,radiology.getImage());
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
        return radiologyDataArrayList.size();
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

