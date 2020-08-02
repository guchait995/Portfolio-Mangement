package com.example.portfoliomanagement.model;

import android.os.Build;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.portfoliomanagement.util.DateConverter;
import com.example.portfoliomanagement.util.Utils;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "investment_statement")
public class InvestmentStatement {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "investment_date")
    @TypeConverters(DateConverter.class)
    private Date investmentDate;

    @ColumnInfo(name = "current_net_worth")
    private double currentNetWorth;

    @ColumnInfo(name = "todays_gain")
    private double todaysGain;

    @ColumnInfo(name = "today_investment")
    private double todayInvestment;

    @ColumnInfo(name = "unreliesed_gains")
    private double unreliesedGains;

    @ColumnInfo(name = "reliesed_gains")
    private double reliesedGains;

    @ColumnInfo(name = "investment_till_date")
    private double investmentTillDate;


    public InvestmentStatement() {
    }

    public InvestmentStatement(Date investmentDate, double currentNetWorth, double todaysGain, double todayInvestment, double unreliesedGains, double reliesedGains, double investmentTillDate) {
        this.investmentDate = investmentDate;
        this.currentNetWorth = currentNetWorth;
        this.todaysGain = todaysGain;
        this.todayInvestment = todayInvestment;
        this.unreliesedGains = unreliesedGains;
        this.reliesedGains = reliesedGains;
        this.investmentTillDate = investmentTillDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getInvestmentDate() {
        return investmentDate;
    }

    public void setInvestmentDate(Date investmentDate) {
        this.investmentDate = investmentDate;
    }

    public double getCurrentNetWorth() {
        return currentNetWorth;
    }

    public void setCurrentNetWorth(double currentNetWorth) {
        this.currentNetWorth = currentNetWorth;
    }

    public double getTodaysGain() {
        return todaysGain;
    }

    public void setTodaysGain(double todaysGain) {
        this.todaysGain = todaysGain;
    }

    public double getTodayInvestment() {
        return todayInvestment;
    }

    public void setTodayInvestment(double todayInvestment) {
        this.todayInvestment = todayInvestment;
    }

    public double getUnreliesedGains() {
        return unreliesedGains;
    }

    public void setUnreliesedGains(double unreliesedGains) {
        this.unreliesedGains = unreliesedGains;
    }

    public double getReliesedGains() {
        return reliesedGains;
    }

    public void setReliesedGains(double reliesedGains) {
        this.reliesedGains = reliesedGains;
    }

    public double getInvestmentTillDate() {
        return investmentTillDate;
    }

    public void setInvestmentTillDate(double investmentTillDate) {
        this.investmentTillDate = investmentTillDate;
    }

    public InvestmentStatement clone() {
        return new InvestmentStatement(this.investmentDate, this.currentNetWorth, this.todaysGain, this.todayInvestment, this.unreliesedGains, this.reliesedGains, this.investmentTillDate);
    }

    public static boolean isInstanceOf(String investmentRecordString) {
        return InvestmentStatement.class.getName().equals(investmentRecordString.split(",")[0]);
    }

    public static InvestmentStatement fromString(String investmentRecordString) {
        String[] investmentRecordStringArr = investmentRecordString.split(",");
        int id = Integer.parseInt(investmentRecordStringArr[1]);
        Date iDate = Utils.getDateFromString(investmentRecordStringArr[2]);
        double cNWorth = Double.parseDouble(investmentRecordStringArr[3]);
        double tGain = Double.parseDouble(investmentRecordStringArr[4]);
        double tInv = Double.parseDouble(investmentRecordStringArr[5]);
        double uGain = Double.parseDouble(investmentRecordStringArr[6]);
        double rGain = Double.parseDouble(investmentRecordStringArr[7]);
        double iTillDate = Double.parseDouble(investmentRecordStringArr[8]);

        InvestmentStatement investmentStatement = new InvestmentStatement(iDate, cNWorth, tGain, tInv, uGain, rGain, iTillDate);
        investmentStatement.setId(id);
        return investmentStatement;
    }

    public String getBackupRecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join(",",
                    this.getClass().getName(),
                    String.valueOf(this.id),
                    String.valueOf(Utils.getStringFromDate(this.investmentDate)),
                    String.valueOf(this.currentNetWorth),
                    String.valueOf(this.todaysGain),
                    String.valueOf(this.todayInvestment),
                    String.valueOf(this.unreliesedGains),
                    String.valueOf(this.reliesedGains),
                    String.valueOf(this.investmentTillDate)
            );
        }
        return null;
    }
}
