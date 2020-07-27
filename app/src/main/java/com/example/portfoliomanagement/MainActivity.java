package com.example.portfoliomanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.portfoliomanagement.adapter.StatementListAdapter;
import com.example.portfoliomanagement.dao.AppDatabase;
import com.example.portfoliomanagement.model.InvestmentStatement;
import com.example.portfoliomanagement.service.DBService;
import com.example.portfoliomanagement.service.InvestmentStatementService;
import com.example.portfoliomanagement.util.BackupRestoreHelper;
import com.example.portfoliomanagement.util.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<InvestmentStatement> investmentStatements;
    private Map<Date,InvestmentStatement> dateInvestmentStatementMap;
    InvestmentStatement lastStatement;
    InvestmentStatementService investmentStatementService;
    public RecyclerView statementRecyclerView;
    public TextView monthlyProfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DBService.init(this);
        investmentStatements=new LinkedList<>();
        statementRecyclerView=findViewById(R.id.statementList);
        monthlyProfit=findViewById(R.id.monthly_profit);
        investmentStatementService=new InvestmentStatementService(this);
        if(!investmentStatements.isEmpty()){
            lastStatement=investmentStatements.get(investmentStatements.size()-1);
        }
        if(!Utils.checkPermissions(this)){
            Toast.makeText(this, "Permissions Not Granted", Toast.LENGTH_SHORT).show();
        }
        dateInvestmentStatementMap=Utils.investmentStatementDateListToMap(investmentStatements);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            addInvestmentStatement(view,null);
            }
        });
        statementRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       refreshInvestments();

    }

    public void refreshInvestments(){
        investmentStatements=investmentStatementService.getAllInvestmentsDESC(statementRecyclerView,monthlyProfit);
//        investmentStatementService.getAllInvestmentsASC(statementRecyclerView,monthlyProfit);
    }

    public void deleteInvestment(InvestmentStatement investmentStatement){
        investmentStatementService.deleteStatement(investmentStatement);
    }

    private void addInvestmentStatement(View v,InvestmentStatement investmentStatement){

        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
//        builder.setTitle("Add Invest Statement");
        final View customLayout = getLayoutInflater().inflate(R.layout.add_investment_statement, null);
        builder.setView(customLayout);
        final EditText etTodaysInvestment = customLayout.findViewById(R.id.todaysInvestment);
        final EditText etTotalValue = customLayout.findViewById(R.id.totalValue);
        final EditText etTodaysGain = customLayout.findViewById(R.id.todaysGain);
        final EditText etDate = customLayout.findViewById(R.id.statementDate);
        final EditText etInvestmentTillDate = customLayout.findViewById(R.id.investment_till_date);
        final EditText etUnreliesedGain = customLayout.findViewById(R.id.unreliesedGain);
        final CalendarView calendarView= customLayout.findViewById(R.id.datePicker);
        autoInvestmentStatementCreation(
                etTodaysInvestment,etTotalValue,
                etTodaysGain,etDate,
                etInvestmentTillDate,etUnreliesedGain,
                calendarView);

        etDate.setEnabled(false);
        etDate.setText(Utils.getTodaysDate());
        if(investmentStatement!=null){
            etTodaysInvestment.setText(String.valueOf(investmentStatement.getTodayInvestment()));
            etTotalValue.setText(String.valueOf(investmentStatement.getCurrentNetWorth()));
            etTodaysGain.setText(String.valueOf(investmentStatement.getTodaysGain()));
            etDate.setText(Utils.getStringFromDate(investmentStatement.getInvestmentDate()));
            etInvestmentTillDate.setText(String.valueOf(investmentStatement.getInvestmentTillDate()));
            etUnreliesedGain.setText(String.valueOf(investmentStatement.getUnreliesedGains()));
            calendarView.setDate(investmentStatement.getInvestmentDate().getTime());
        }else{
            investmentStatement= new InvestmentStatement();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    etDate.setText(Utils.dateToString(year,month,dayOfMonth));
                }
            });
        }
        final InvestmentStatement investmentStatementTemp=investmentStatement;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date statementDate= Utils.stringToDate(etDate.getText().toString(),Utils.getAppDateFormat());
                Double todaysInvestment=Double.parseDouble(etTodaysInvestment.getText().toString());
                Double currentNetWorth=Double.parseDouble(etTotalValue.getText().toString());
                Double todaysGain=Double.parseDouble(etTodaysGain.getText().toString());
                Double investmentTillDate=Double.parseDouble(etInvestmentTillDate.getText().toString());
                Double unreliesedGain=Double.parseDouble(etUnreliesedGain.getText().toString());



                investmentStatementTemp.setCurrentNetWorth(currentNetWorth);
                investmentStatementTemp.setInvestmentDate(statementDate);
                investmentStatementTemp.setTodayInvestment(todaysInvestment);
                investmentStatementTemp.setTodaysGain(todaysGain);
                investmentStatementTemp.setInvestmentTillDate(investmentTillDate);
                investmentStatementTemp.setUnreliesedGains(unreliesedGain);
                investmentStatementTemp.setReliesedGains(0);

                InvestmentStatement statementToAdd =
                        Utils.createInvestmentStatment(lastStatement,investmentStatementTemp);

                investmentStatementService.addInvestment(statementToAdd);
                refreshInvestments();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void editInvestmentStatement(View v,InvestmentStatement investmentStatement){
        addInvestmentStatement(v,investmentStatement);
    }

    public void autoInvestmentStatementCreation(
            EditText etTodaysInvestment,
            EditText etTotalValue,
            EditText etTodaysGain,
            EditText etDate,
            EditText etInvestmentTillDate,
            EditText etUnreliesedGain,
            CalendarView calendarView
            ){

        if(investmentStatements.size()>0){
            lastStatement=investmentStatements.get(investmentStatements.size()-1);
            InvestmentStatement newInvestmentStatement=lastStatement.clone();
            etTodaysInvestment.setText(String.valueOf(0));
            etTodaysGain.setText(String.valueOf(0));
            etTodaysInvestment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>0 && Utils.isDouble(s.toString())) {
                        double todaysInvestment = Double.valueOf(s.toString());
                        newInvestmentStatement.setTodayInvestment(todaysInvestment);
                        newInvestmentStatement.setInvestmentTillDate(investmentStatements.get(0).getInvestmentTillDate() +
                                todaysInvestment
                        );
                        etInvestmentTillDate.setText(String.valueOf(newInvestmentStatement.getInvestmentTillDate()));
                    }
                }
            });
            etTodaysGain.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>0 && Utils.isDouble(s.toString())) {
                        double todaysGain = Double.valueOf(s.toString());
                        newInvestmentStatement.setTodaysGain(todaysGain);
                        newInvestmentStatement.setUnreliesedGains(investmentStatements.get(0).getUnreliesedGains() +
                                todaysGain
                        );
                        etUnreliesedGain.setText(String.valueOf(newInvestmentStatement.getUnreliesedGains()));
                    }
                }
            });
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_backup) {
            new BackupRestoreHelper.CreateBackup(this).execute();
            return true;
        }
        if (id == R.id.action_restore) {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("*/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(chooseFile, Utils.PICKFILE_RESULT_CODE);
            return true;
        }
        if (id == R.id.action_password) {
            Intent intent = new Intent(this,PasswordListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Utils.PICKFILE_RESULT_CODE){
            new BackupRestoreHelper.startRestore(this,data.getData()).execute();
        }
    }
}
