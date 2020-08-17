package com.example.portfoliomanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfoliomanagement.R;
import com.example.portfoliomanagement.model.UserContactDetail;
import com.example.portfoliomanagement.util.ContactsUtils;

import java.util.List;

public class ContactsAdapter  extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>  {

    private List<UserContactDetail> userContactDetails;
    private Context context;
    int actionType;
    public ContactsAdapter(Context context, List<UserContactDetail> userContactDetails,int actionType){
        this.userContactDetails=userContactDetails;
        this.context=context;
        this.actionType=actionType;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.each_contact_layout, parent, false);
        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(v);
        return contactsViewHolder;
    }

    @Override
    public int getItemCount() {
        return userContactDetails.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactsViewHolder holder, int position) {
        UserContactDetail userContactDetail=userContactDetails.get(position);
        if(actionType== ContactsUtils.ACTION_UPDATE){
            holder.tvName.setText(userContactDetail.getName()+" -> "+userContactDetail.getNonRedundentName(userContactDetail.getName()));
        }else{
            holder.tvName.setText(userContactDetail.getName());
        }

        holder.tvPhone.setText(userContactDetail.getPhoneNumber());
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPhone;
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvEachContactName);
            tvPhone=itemView.findViewById(R.id.tvEachContactNumber);
        }
    }
}
