package com.example.portfoliomanagement.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.portfoliomanagement.MainActivity;
import com.example.portfoliomanagement.adapter.StatementListAdapter;
import com.example.portfoliomanagement.dao.AppDatabase;
import com.example.portfoliomanagement.dao.InvestmentStatementDao;
import com.example.portfoliomanagement.model.InvestmentStatement;
import com.example.portfoliomanagement.util.FinanceCalculations;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InvestmentStatementService {
    private AppDatabase appDatabase;
    public InvestmentStatementDao investmentStatementDao;
    private Context context;
    public InvestmentStatementService(Context context){
        appDatabase= DBService.getAppDatabase();
        investmentStatementDao=appDatabase.investmentStatementDao();
        this.context=context;
    }

    public void addInvestment(InvestmentStatement investmentStatement){
       new AddDBTasks(investmentStatementDao).execute(investmentStatement);
    }
    public List<InvestmentStatement> getAllInvestmentsDESC(RecyclerView recyclerView, TextView monthlyProfit){
        try {
            return new FetchDBTasksDESC(investmentStatementDao,recyclerView,monthlyProfit,context).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<InvestmentStatement> getAllInvestments(){
        try {
            return new FetchDBTasks(investmentStatementDao,context).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteStatement(InvestmentStatement investmentStatement){
        new DeleteDBTask(investmentStatementDao,context).execute(investmentStatement);
    }

    public void addAllInvestments(List<InvestmentStatement> investmentStatements){
        new AddAllDBTasks(investmentStatementDao).execute(investmentStatements);
    }


    class FetchDBTasks extends AsyncTask<Void,Void,List<InvestmentStatement> >{
        private InvestmentStatementDao investmentStatementDao;
        private RecyclerView recyclerView;
        private Context context;
        private TextView monthlyProfit;
        FetchDBTasks(InvestmentStatementDao investmentStatementDao,Context context){
            this.investmentStatementDao=investmentStatementDao;
            this.context=context;
        }

        @Override
        protected List<InvestmentStatement> doInBackground(Void... voids) {
            return investmentStatementDao.getAllDateASC();
        }
    }
    class FetchDBTasksDESC extends AsyncTask<Void,Void,List<InvestmentStatement> >{
        private InvestmentStatementDao investmentStatementDao;
        private RecyclerView recyclerView;
        private Context context;
        private TextView monthlyProfit;
        FetchDBTasksDESC(InvestmentStatementDao investmentStatementDao,RecyclerView recyclerView,TextView monthlyProfit,Context context){
            this.investmentStatementDao=investmentStatementDao;
            this.recyclerView=recyclerView;
            this.context=context;
            this.monthlyProfit=monthlyProfit;
        }

        @Override
        protected List<InvestmentStatement> doInBackground(Void... voids) {
            return investmentStatementDao.getAllDateDESC();
        }

        @Override
        protected void onPostExecute(List<InvestmentStatement> investmentStatements) {
            super.onPostExecute(investmentStatements);
            StatementListAdapter sla=new StatementListAdapter(context,investmentStatements);
            recyclerView.setAdapter(sla);
            monthlyProfit.setText("Profit: "+String.valueOf(FinanceCalculations.monthlyProfit(investmentStatements)));
        }
    }

    class DeleteDBTask extends AsyncTask<InvestmentStatement,InvestmentStatement,Void>{

        private InvestmentStatementDao investmentStatementDao;
        private Context context;
        DeleteDBTask(InvestmentStatementDao investmentStatementDao,Context context){
            this.investmentStatementDao=investmentStatementDao;
            this.context=context;
        }
        @Override
        protected Void doInBackground(InvestmentStatement... investmentStatement) {
            investmentStatementDao.delete(investmentStatement[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((MainActivity) context).refreshInvestments();
        }
    }
    class AddDBTasks extends AsyncTask<InvestmentStatement,InvestmentStatement,Void>{
        private InvestmentStatementDao investmentStatementDao;
        AddDBTasks(InvestmentStatementDao investmentStatementDao){
            this.investmentStatementDao=investmentStatementDao;

        }

        @Override
        protected Void doInBackground(InvestmentStatement... investmentStatement) {
            investmentStatementDao.save(investmentStatement[0]);
            return null;
        }
    }
    class AddAllDBTasks extends AsyncTask<List<InvestmentStatement>,List<InvestmentStatement>,Void>{
        private InvestmentStatementDao investmentStatementDao;
        AddAllDBTasks(InvestmentStatementDao investmentStatementDao){
            this.investmentStatementDao=investmentStatementDao;

        }

        @Override
        protected Void doInBackground(List<InvestmentStatement>... investmentStatements) {
            investmentStatementDao.insertAll(investmentStatements[0]);
            return null;
        }
    }



}

