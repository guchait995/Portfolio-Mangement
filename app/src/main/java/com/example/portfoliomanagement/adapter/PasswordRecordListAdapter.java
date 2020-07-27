package com.example.portfoliomanagement.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfoliomanagement.PasswordListActivity;
import com.example.portfoliomanagement.R;
import com.example.portfoliomanagement.model.PasswordRecord;
import com.example.portfoliomanagement.util.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PasswordRecordListAdapter extends  RecyclerView.Adapter<PasswordRecordListAdapter.PasswordRecordViewHolder>{

    private List<PasswordRecord> passwordRecords;
    private Context context;
    public PasswordRecordListAdapter(List<PasswordRecord> passwordRecords, Context context){
        List<PasswordRecord> passwordRecordsTemp=new LinkedList<>();
        passwordRecordsTemp.add(new PasswordRecord());
        passwordRecordsTemp.addAll(passwordRecords);
        this.passwordRecords=passwordRecordsTemp;
        this.context=context;
    }


    @NonNull
    @Override
    public PasswordRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.each_password_record,parent,false);
        PasswordRecordViewHolder passwordRecordViewHolder=new PasswordRecordViewHolder(v);
        return passwordRecordViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordRecordViewHolder holder, int position) {
        if(passwordRecords.get(position).getApplication()==null){
            holder.tvApplication.setText("Application");
            holder.tvApplication.setTypeface(null, Typeface.BOLD);
            holder.tvPasswordType.setText("UserId");
            holder.tvPasswordType.setTypeface(null, Typeface.BOLD);
            holder.tvPassword.setText("Password");
            holder.tvPassword.setTypeface(null, Typeface.BOLD);
            holder.deleteButton.setVisibility(View.INVISIBLE);
            holder.deleteButton.setEnabled(false);

        }else{
            PasswordRecord passwordRecord=passwordRecords.get(position);
            holder.tvApplication.setText(passwordRecord.getApplication());
            holder.tvPasswordType.setText(passwordRecord.getPasswordType());
            holder.tvPassword.setText(passwordRecord.getPassword());
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setEnabled(true);

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.isConfirmedToDelete(context,passwordRecord);
                }
            });
            holder.eachPasswordRecordBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PasswordListActivity)context).editPasswordRecord(passwordRecord);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return passwordRecords.size();
    }

    public static class PasswordRecordViewHolder extends RecyclerView.ViewHolder{
        TextView tvApplication;
        TextView tvPasswordType;
        TextView tvPassword;
        ImageView deleteButton;
        LinearLayout eachPasswordRecordBody;
        public PasswordRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvApplication=itemView.findViewById(R.id.password_record_application);
            tvPasswordType=itemView.findViewById(R.id.password_record_type);
            tvPassword=itemView.findViewById(R.id.password_record_password);
            deleteButton=itemView.findViewById(R.id.delete_password_record);
            eachPasswordRecordBody=itemView.findViewById(R.id.each_password_record_body);
        }
    }
}
