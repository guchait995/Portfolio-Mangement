package com.example.portfoliomanagement.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.portfoliomanagement.model.InvestmentStatement;
import com.example.portfoliomanagement.model.PasswordRecord;

@Database(entities = {InvestmentStatement.class, PasswordRecord.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract InvestmentStatementDao investmentStatementDao();

    public abstract PasswordRecordDao passwordRecordDao();
}