package com.example.portfoliomanagement;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portfoliomanagement.adapter.ContactsAdapter;
import com.example.portfoliomanagement.model.UserContactDetail;
import com.example.portfoliomanagement.responses.UpdationResponse;
import com.example.portfoliomanagement.util.ContactsUtils;

public class ContactsFragment extends Fragment {
    private Button btnCheckContacts;
    private TextView tvUpdateContacts;
    private TextView tvDeleteContacts;
    private ProgressBar progressBar;
    private LinearLayout detailsLayout;
    public UpdationResponse.Status status= null;
    public ContactsFragment contactsFragment;
    private Button btnBatchUpdate;
    private Button btnBatchDelete;

    public final String TAG = "ContactsFragment-1995";
    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contacts, container, false);
        btnCheckContacts=view.findViewById(R.id.check_contacts);
        tvUpdateContacts=view.findViewById(R.id.update_contacts_value);
        tvDeleteContacts=view.findViewById(R.id.delete_contacts_value);
        detailsLayout=view.findViewById(R.id.updation_details_layout);
        progressBar=view.findViewById(R.id.progress_circular);
        btnBatchDelete=view.findViewById(R.id.btn_process_delete);
        btnBatchUpdate=view.findViewById(R.id.btn_process_update);
        progressBar.setVisibility(View.INVISIBLE);
        contactsFragment=this;
        btnCheckContacts.setText("Refresh Contacts");
        progressBar.setVisibility(View.GONE);
        detailsLayout.setVisibility(View.GONE);
        new RefreshContactsTask(view,contactsFragment,false,-1).execute();

        btnCheckContacts.setVisibility(View.GONE);
        btnBatchUpdate.setOnClickListener(v->{
            //process updates
            new RefreshContactsTask(view,contactsFragment,true,ContactsUtils.ACTION_UPDATE).execute();

        });
        btnBatchDelete.setOnClickListener(v->{
            //process deletes
            new RefreshContactsTask(view,contactsFragment,true,ContactsUtils.ACTION_DELETE).execute();

        });
        return view;
    }

    private class RefreshContactsTask extends AsyncTask<Void,Void, UpdationResponse>{
        private TextView tvUpdateContacts;
        private TextView tvDeleteContacts;
        private ProgressBar progressBar;
        private LinearLayout detailsLayout;
        private RecyclerView updationList;
        private RecyclerView deletionList;
        private Button btnCheckContacts;
        private Button btnBatchUpdate;
        private Button btnBatchDelete;
        View view;
        ContactsFragment contactsFragment;
        boolean takeAction;
        int actionType;
        RefreshContactsTask(View view, ContactsFragment contactsFragment,boolean takeAction,int actionType){
            this.view=view;
            this.contactsFragment=contactsFragment;
            this.takeAction=takeAction;
            this.actionType=actionType;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvUpdateContacts=view.findViewById(R.id.update_contacts_value);
            tvDeleteContacts=view.findViewById(R.id.delete_contacts_value);
            detailsLayout=view.findViewById(R.id.updation_details_layout);
            updationList=view.findViewById(R.id.rcUpdationList);
            deletionList=view.findViewById(R.id.rcDeletetionList);
            progressBar=view.findViewById(R.id.progress_circular);
            progressBar.setVisibility(View.VISIBLE);
            detailsLayout.setVisibility(View.GONE);
            updationList.setLayoutManager(new LinearLayoutManager(contactsFragment.getActivity()));
            deletionList.setLayoutManager(new LinearLayoutManager(contactsFragment.getActivity()));
            btnCheckContacts=view.findViewById(R.id.check_contacts);
            btnBatchDelete=view.findViewById(R.id.btn_process_delete);
            btnBatchUpdate=view.findViewById(R.id.btn_process_update);

        }

        @Override
        protected void onPostExecute(UpdationResponse updationResponse) {
            super.onPostExecute(updationResponse);
            if(takeAction){
                new RefreshContactsTask(view,contactsFragment,false,-1).execute();
            }else {
                Log.d(TAG, String.valueOf(updationResponse.getHasToDeleted().size()));
                progressBar.setVisibility(View.GONE);
                detailsLayout.setVisibility(View.VISIBLE);
                tvUpdateContacts.setText(String.valueOf(updationResponse.getHasToUpdated().size()));
                tvDeleteContacts.setText(String.valueOf(updationResponse.getHasToDeleted().size()));
                ContactsAdapter updateContactAdapter = new ContactsAdapter(getContext(), updationResponse.getHasToUpdated(),ContactsUtils.ACTION_UPDATE);
                ContactsAdapter deleteContactAdapter = new ContactsAdapter(getContext(), updationResponse.getHasToDeleted(),ContactsUtils.ACTION_DELETE);
                updationList.setAdapter(updateContactAdapter);
                deletionList.setAdapter(deleteContactAdapter);
                Toast.makeText(contactsFragment.getContext(), "Completed", Toast.LENGTH_SHORT).show();
                btnBatchUpdate.setEnabled(updationResponse.getHasToUpdated().size() > 0);
                btnBatchDelete.setEnabled(updationResponse.getHasToDeleted().size() > 0);
            }
        }

        @Override
        protected UpdationResponse doInBackground(Void... voids) {
            return ContactsUtils.checkAllContacts(getContext(),takeAction,actionType);
        }
    }
}
