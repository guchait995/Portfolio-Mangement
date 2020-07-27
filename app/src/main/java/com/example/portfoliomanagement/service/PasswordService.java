package com.example.portfoliomanagement.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.portfoliomanagement.adapter.PasswordRecordListAdapter;
import com.example.portfoliomanagement.dao.AppDatabase;
import com.example.portfoliomanagement.dao.PasswordRecordDao;
import com.example.portfoliomanagement.model.PasswordRecord;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PasswordService {
    private AppDatabase appDatabase;
    public PasswordRecordDao passwordRecordDao;
    private Context context;
    public PasswordService(Context context){
        appDatabase=DBService.getAppDatabase();
        passwordRecordDao=appDatabase.passwordRecordDao();
        this.context=context;
    }

    public void addPasswordRecord(PasswordRecord passwordRecord){
        new AddPasswordRecordTask().execute(passwordRecord);
    }
    public void fetchPassword(RecyclerView recyclerView){
        new FetchPasswordTasks(recyclerView).execute();
    }
    public List<PasswordRecord> getAllPasswords(){
        try {
            return new getAllPasswordTasks().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void deletePassword(PasswordRecord passwordRecord,RecyclerView recyclerView){
        new DeletePasswordRecordTask(recyclerView).execute(passwordRecord);
    }
    public void addAllPasswordRecord(List<PasswordRecord> passwordRecords){
        new AddAllPasswordRecordTask().execute(passwordRecords);
    }
    private  class DeletePasswordRecordTask extends AsyncTask<PasswordRecord,Void,Void>{

        RecyclerView recyclerView;
        DeletePasswordRecordTask(RecyclerView recyclerView){
            this.recyclerView=recyclerView;
        }

        @Override
        protected Void doInBackground(PasswordRecord... passwordRecords) {
            passwordRecordDao.delete(passwordRecords[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            fetchPassword(recyclerView);
        }
    }
    private class FetchPasswordTasks extends AsyncTask<Void,Void,List<PasswordRecord>>{
        private RecyclerView recyclerView;
        FetchPasswordTasks(RecyclerView recyclerView){
            this.recyclerView=recyclerView;
        }
        @Override
        protected List<PasswordRecord> doInBackground(Void... voids) {
            return passwordRecordDao.getAll();
        }

        @Override
        protected void onPostExecute(List<PasswordRecord> passwordRecords) {
            super.onPostExecute(passwordRecords);
            PasswordRecordListAdapter passwordRecordListAdapter=
                    new PasswordRecordListAdapter(passwordRecords,context);
            recyclerView.setAdapter(passwordRecordListAdapter);
        }
    }
    private class getAllPasswordTasks extends AsyncTask<Void,Void,List<PasswordRecord>>{
        @Override
        protected List<PasswordRecord> doInBackground(Void... voids) {
            return passwordRecordDao.getAll();
        }
    }
    private class AddPasswordRecordTask extends AsyncTask<PasswordRecord,Void,Void>{

        @Override
        protected Void doInBackground(PasswordRecord... passwordRecords) {
            passwordRecordDao.save(passwordRecords[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Password Successfully Saved!!", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddAllPasswordRecordTask extends AsyncTask<List<PasswordRecord>,Void,Void>{

        @Override
        protected Void doInBackground(List<PasswordRecord>... passwordRecords) {
            passwordRecordDao.insertAll(passwordRecords[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Password Successfully Saved!!", Toast.LENGTH_SHORT).show();
        }
    }

}
