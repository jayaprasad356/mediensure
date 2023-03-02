package com.graymatter.mediensure.fragments;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.graymatter.mediensure.R;
import com.graymatter.mediensure.adapter.PendingDentalAdapter;
import com.graymatter.mediensure.adapter.PendingLabAdapter;
import com.graymatter.mediensure.adapter.PendingOPDAdapter;
import com.graymatter.mediensure.adapter.PendingPharmacyAdapter;
import com.graymatter.mediensure.adapter.PendingRadiologyAdapter;
import com.graymatter.mediensure.helper.ApiConfig;
import com.graymatter.mediensure.helper.Constant;
import com.graymatter.mediensure.helper.Session;
import com.graymatter.mediensure.model.DentalData;
import com.graymatter.mediensure.model.LabData;
import com.graymatter.mediensure.model.OpdData;
import com.graymatter.mediensure.model.PharmacyData;
import com.graymatter.mediensure.model.RadiologyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PendingFragment extends Fragment {

    Chip chipOPD,chipLab,chipPharmacy,chipDental,chipRadiology;
    Activity activity;
    PendingRadiologyAdapter pendingRadiologyAdapter;
    PendingOPDAdapter pendingOPDAdapter;
    PendingDentalAdapter pendingDentalAdapter;
    PendingPharmacyAdapter pendingPharmacyAdapter;
    PendingLabAdapter pendingLabAdapter;
    RecyclerView rvRadiology,rvOpd,rvPharmacy,rvLab,rvDental;
    Session session;
    String status="0";
    public PendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_pending, container, false);
        activity=getActivity();
        session= new Session(activity);
        chipOPD=root.findViewById(R.id.chipOPD);
        chipLab=root.findViewById(R.id.chipLabNetwork);
        chipPharmacy=root.findViewById(R.id.chipPharmacy);
        chipDental=root.findViewById(R.id.chipDental);
        chipRadiology=root.findViewById(R.id.chipRadiology);
        rvRadiology=root.findViewById(R.id.rvRadiology);
        rvDental=root.findViewById(R.id.rvDental);
        rvLab=root.findViewById(R.id.rvLab);
        rvOpd=root.findViewById(R.id.rvOpd);
        rvPharmacy=root.findViewById(R.id.rvPharmacy);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        LinearLayoutManager layoutManagerDental = new LinearLayoutManager(activity);
        LinearLayoutManager layoutManagerOpd = new LinearLayoutManager(activity);
        LinearLayoutManager layoutManagerPharmacy = new LinearLayoutManager(activity);
        LinearLayoutManager layoutManagerLab = new LinearLayoutManager(activity);

        rvRadiology.setLayoutManager(layoutManager);
        rvDental.setLayoutManager(layoutManagerDental);
        rvOpd.setLayoutManager(layoutManagerOpd);
        rvPharmacy.setLayoutManager(layoutManagerPharmacy);
        rvLab.setLayoutManager(layoutManagerLab);

        // initial load
        rvOpd.setVisibility(View.VISIBLE);
        chipOPD.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.lightGree)));
        loadOPDdata();

        chipOPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroudsandVisibals();
                rvOpd.setVisibility(View.VISIBLE);
                chipOPD.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.lightGree)));
                loadOPDdata();
            }
        });
        chipDental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroudsandVisibals();
                rvDental.setVisibility(View.VISIBLE);
                chipDental.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.lightGree)));
                loadDentaldata();
            }
        });

        chipLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroudsandVisibals();
                rvLab.setVisibility(View.VISIBLE);
                chipLab.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.lightGree)));
                loadLabData();
            }
        });
        chipPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroudsandVisibals();
                rvPharmacy.setVisibility(View.VISIBLE);
                chipPharmacy.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.lightGree)));
                loadPharmacydata();
            }
        });
        chipRadiology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroudsandVisibals();
                rvRadiology.setVisibility(View.VISIBLE);
                chipRadiology.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.lightGree)));
                loadRadiologyData();
            }
        });

        return root;
    }
    void setBackgroudsandVisibals(){
        chipOPD.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.grey)));
        chipLab.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.grey)));
        chipPharmacy.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.grey)));
        chipDental.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.grey)));
        chipRadiology.setChipBackgroundColor(ColorStateList.valueOf(activity.getResources().getColor(R.color.grey)));
        rvPharmacy.setVisibility(View.GONE);
        rvOpd.setVisibility(View.GONE);
        rvLab.setVisibility(View.GONE);
        rvRadiology.setVisibility(View.GONE);
        rvDental.setVisibility(View.GONE);
    }

    private void loadRadiologyData() {
        Map<String, String> params = new HashMap<>();

        params.put(Constant.USER_ID,session.getData(Constant.ID));
        params.put(Constant.STATUS,status);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("radioloy list",response);

                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<RadiologyData> radiologyDatas = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                RadiologyData radiologyData = g.fromJson(jsonObject1.toString(), RadiologyData.class);
                                radiologyDatas.add(radiologyData);
                            } else {
                                break;
                            }
                        }

                        pendingRadiologyAdapter = new PendingRadiologyAdapter(activity, radiologyDatas);
                        rvRadiology.setAdapter(pendingRadiologyAdapter);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.RADIOLOGY_NETWORK_LIST, params, true);


    }
    private void loadOPDdata() {
        Map<String, String> params = new HashMap<>();

        params.put(Constant.USER_ID,session.getData(Constant.ID));
        params.put(Constant.STATUS,status);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("OPD list",response);

                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<OpdData> opdDataArrayList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                OpdData opdData = g.fromJson(jsonObject1.toString(), OpdData.class);
                                opdDataArrayList.add(opdData);
                            } else {
                                break;
                            }
                        }

                        pendingOPDAdapter = new PendingOPDAdapter(activity, opdDataArrayList);
                        rvOpd.setAdapter(pendingOPDAdapter);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.OPD_NETWORK_LIST, params, true);


    }
    private void loadDentaldata() {
        Map<String, String> params = new HashMap<>();

        params.put(Constant.USER_ID,session.getData(Constant.ID));
        params.put(Constant.STATUS,status);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("OPD list",response);

                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<DentalData> dentalDataArrayList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                DentalData dentalData = g.fromJson(jsonObject1.toString(), DentalData.class);
                                dentalDataArrayList.add(dentalData);
                            } else {
                                break;
                            }
                        }

                        pendingDentalAdapter = new PendingDentalAdapter(activity, dentalDataArrayList);
                        rvDental.setAdapter(pendingDentalAdapter);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.DENTAL_NETWORK_LIST, params, true);


    }
    private void loadPharmacydata() {
        Map<String, String> params = new HashMap<>();

        params.put(Constant.USER_ID,session.getData(Constant.ID));
        params.put(Constant.STATUS,status);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("pharmacy list",response);

                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<PharmacyData> pharmacyDataArrayList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                PharmacyData pharmacyData = g.fromJson(jsonObject1.toString(), PharmacyData.class);
                                pharmacyDataArrayList.add(pharmacyData);
                            } else {
                                break;
                            }
                        }

                        pendingPharmacyAdapter = new PendingPharmacyAdapter(activity, pharmacyDataArrayList);
                        rvPharmacy.setAdapter(pendingPharmacyAdapter);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.PHARMACY_NETWORK_LIST, params, true);

    }
    private void loadLabData() {
        Map<String, String> params = new HashMap<>();

        params.put(Constant.USER_ID,session.getData(Constant.ID));
        params.put(Constant.STATUS,status);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Log.d("radioloy list",response);

                        JSONObject object = new JSONObject(response);
                        JSONArray jsonArray = object.getJSONArray(Constant.DATA);
                        Gson g = new Gson();
                        ArrayList<LabData> labDataArrayList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                LabData labData = g.fromJson(jsonObject1.toString(), LabData.class);
                                labDataArrayList.add(labData);
                            } else {
                                break;
                            }
                        }

                        pendingLabAdapter = new PendingLabAdapter(activity, labDataArrayList);
                        rvLab.setAdapter(pendingLabAdapter);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.LAB_NETWORK_LIST, params, true);


    }
}