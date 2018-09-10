package com.caliber.shwaasdemo.Model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.caliber.shwaasdemo.R;

import java.util.List;

/**
 * Created by Caliber on 18-01-2018.
 */

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder>  {
    private List<Patient> patientList;
    private Context context;
    private ClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, gender,birthYear,createdOn;
        public Button view;


        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            gender = itemView.findViewById(R.id.gender);
            birthYear = itemView.findViewById(R.id.birthyear);
            createdOn = itemView.findViewById(R.id.createdTextId);
            view = itemView.findViewById(R.id.linearlayout);
        }
    }
    public PatientAdapter(Context context, List<Patient> patientList, Activity activity){
        this.context = context;
        this.patientList = patientList;
        this.listener = (ClickListener) activity;
    }

    @Override
    public PatientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PatientAdapter.MyViewHolder holder, final int position) {
        final Patient patient = patientList.get(position);
        holder.name.setText(patient.getName());
        holder.gender.setText(patient.getGender());
        holder.birthYear.setText(patient.getBirthYear());
        holder.createdOn.setText("Created on: "+patient.getCreatedOn());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(patient);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(patient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }
    public void setFilter(List<Patient> countryModels) {
        patientList = countryModels;
        notifyDataSetChanged();
    }

}
