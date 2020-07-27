package com.example.portfoliomanagement.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.portfoliomanagement.MainActivity;
import com.example.portfoliomanagement.model.InvestmentStatement;
import com.example.portfoliomanagement.model.PasswordRecord;
import com.example.portfoliomanagement.service.InvestmentStatementService;
import com.example.portfoliomanagement.service.PasswordService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BackupRestoreHelper {
    public static class CreateBackup extends AsyncTask<Void, Void, Void> {
        List<Object> objects;
        private Context context;
        private final String TAG="BackupRestoreHelper";
        private InvestmentStatementService investmentStatementService;
        private PasswordService passwordService;
        List<InvestmentStatement> investmentStatements;
        List<PasswordRecord> passwordRecords;
        public CreateBackup(Context context) {
            this.objects=new ArrayList<>();
            this.context=context;


        }

        @Override
        protected Void doInBackground(Void... voids) {
            investmentStatementService=new InvestmentStatementService(context);
            passwordService=new PasswordService(context);
            investmentStatements=investmentStatementService.investmentStatementDao.getAllDateASC();
            passwordRecords=passwordService.passwordRecordDao.getAll();
            objects.addAll(investmentStatements);
            objects.addAll(passwordRecords);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                String filename=context.getExternalFilesDir(null).getAbsolutePath().concat("/").concat(getFileName());
                if(!objects.isEmpty()){
                    //investment statements backup
                    File file=new File(filename);
                    file.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(file);
                    int count=0;
                    for(Object o:objects){
                        String record="";
                        if(o instanceof InvestmentStatement){
                            fOut.write("\n".getBytes());
                            InvestmentStatement investmentStatement=(InvestmentStatement)o;
                            record=investmentStatement.getBackupRecord();
                        }
                        if(o instanceof PasswordRecord){
                            fOut.write("\n".getBytes());
                            PasswordRecord passwordRecord=(PasswordRecord) o;
                            record=passwordRecord.getBackupRecord();
                        }

                        fOut.write(record.getBytes());
                        Log.d(TAG,"Backup Processing - "+(count+1)+"/"+objects.size());
                        count++;
                    }
                    fOut.close();
                    Log.d(TAG,"Backup completed: @"+filename);
                    Toast.makeText(context, "Backup completed successfully!! ", Toast.LENGTH_SHORT).show();

                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Backup Failed!! ", Toast.LENGTH_SHORT).show();

            }


        }
    }


    public static class startRestore extends AsyncTask<Void, Void, Void> {
        List<Object> objects;
        private Context context;
        private final String TAG="BackupRestoreHelper";
        private Uri restoreFilePath;
        private InvestmentStatementService investmentStatementService;
        private PasswordService passwordService;
        public startRestore(Context context, Uri restoreFilePath) {
            this.objects=new ArrayList<>();
            this.context=context;
            this.restoreFilePath=restoreFilePath;
            investmentStatementService=new InvestmentStatementService(context);
            passwordService=new PasswordService(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<InvestmentStatement> investmentStatements=new LinkedList<>();
            List<PasswordRecord> passwordRecords=new LinkedList<>();
            try {
                InputStream is=context.getContentResolver().openInputStream(restoreFilePath);
                String line;
                InputStreamReader isReader = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isReader);
                while ((line = reader.readLine()) != null) {
                    if(InvestmentStatement.isInstanceOf(line)){
                        //invesmet_statement_record
                        investmentStatements.add( InvestmentStatement.fromString(line));
                    }
                    if(PasswordRecord.isInstanceOf(line)){
                        //password_record
                        passwordRecords.add(PasswordRecord.fromString(line));
                    }

                }
                passwordService.addAllPasswordRecord(passwordRecords);
                investmentStatementService.addAllInvestments(investmentStatements);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity mainActivity=(MainActivity)context;
            if(mainActivity!=null && mainActivity.statementRecyclerView!=null && mainActivity.monthlyProfit!=null){
                investmentStatementService.getAllInvestmentsDESC(mainActivity.statementRecyclerView,mainActivity.monthlyProfit);
            }
            Toast.makeText(context, "Restore completed successfully!! ", Toast.LENGTH_SHORT).show();


        }
    }


    public static String getFileName(){
       return String.valueOf(new Date().getTime()).concat("_").concat("backup.bck");
    }



}
