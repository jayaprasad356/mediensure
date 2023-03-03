package com.graymatter.mediensure.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graymatter.mediensure.EditOPDActivity;
import com.graymatter.mediensure.R;
import com.graymatter.mediensure.RadiologyEditActivity;
import com.graymatter.mediensure.helper.Constant;
import com.graymatter.mediensure.helper.Session;
import com.graymatter.mediensure.model.OpdData;
import com.graymatter.mediensure.model.RadiologyData;

import java.util.ArrayList;

public class PendingOPDAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Activity activity;
    final ArrayList<OpdData> opdData;
    Session session;
    String Totalprice = "";


    public PendingOPDAdapter(Activity activity, ArrayList<OpdData> opdData) {
        this.activity = activity;
        this.opdData = opdData;
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
        final OpdData opdata = opdData.get(position);

        holder.tvName.setText(opdata.getName());
        holder.tvMobile.setText(opdata.getMobile());
        holder.tvDateandTime.setText(opdata.getDatetime());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, EditOPDActivity.class);
                intent.putExtra(Constant.ID,opdata.getId());
                intent.putExtra(Constant.USER_ID,opdata.getUser_id());
                intent.putExtra(Constant.NAME,opdata.getName());
                intent.putExtra(Constant.EMAIL,opdata.getEmail());
                intent.putExtra(Constant.MOBILE,opdata.getMobile());
                intent.putExtra(Constant.ADDRESS,opdata.getAddress());
                intent.putExtra(Constant.LATITUDE,opdata.getLatitude());
                intent.putExtra(Constant.LONGITUDE,opdata.getLongitude());
                intent.putExtra(Constant.DATETIME,opdata.getDatetime());
                intent.putExtra(Constant.REMARKS,opdata.getRemarks());
                intent.putExtra(Constant.STATUS,opdata.getStatus());

                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return opdData.size();
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

