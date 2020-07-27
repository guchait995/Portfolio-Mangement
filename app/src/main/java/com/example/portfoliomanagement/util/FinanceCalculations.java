package com.example.portfoliomanagement.util;

import android.os.Build;
import android.text.format.DateFormat;

import com.example.portfoliomanagement.model.InvestmentStatement;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FinanceCalculations {

    public static double monthlyProfit(List<InvestmentStatement> statements){
        if(statements.isEmpty())
            return 0;
        InvestmentStatement lastStatement=statements.get(0);
        Date date=lastStatement.getInvestmentDate();
        String day          = (String) DateFormat.format("dd",   date); // 20
        String monthString  = (String) DateFormat.format("MMM",  date); // Jun
        final String monthNumber  = (String) DateFormat.format("MM",   date); // 06
        final String year         = (String) DateFormat.format("yyyy", date); // 2013
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Optional<InvestmentStatement> statementFirstOftheMonth= statements.stream().filter((statement)->((String)
                    DateFormat.format("MM",
                            statement.getInvestmentDate())).equals(monthNumber)
                    && ((String) DateFormat.format("yyyy",
                    statement.getInvestmentDate())).equals(year)

            ).sorted(new Comparator<InvestmentStatement>() {
                @Override
                public int compare(InvestmentStatement o1, InvestmentStatement o2) {
                    return o1.getInvestmentDate().after(o2.getInvestmentDate())?0:-1;
                }
            }).findFirst();
            if(statementFirstOftheMonth.isPresent()){
               double monthStartUGains= statementFirstOftheMonth.get().getUnreliesedGains();
               double TGains=statementFirstOftheMonth.get().getTodaysGain();
               double gainsBeforeToday=monthStartUGains-TGains;
               double monthTillDateGain = lastStatement.getUnreliesedGains();
               return monthTillDateGain-gainsBeforeToday;
            }
        }
        return 0;
    }

    public static InvestmentStatement getNewInvestmentStatement(
            List<InvestmentStatement> investmentStatements,
            InvestmentStatement newInvestmentStatement
            ){
        for(InvestmentStatement investmentStatement:investmentStatements){
            if(investmentStatements.get(0).getInvestmentDate().equals(investmentStatement.getInvestmentDate())){
                //First we will do nothing
//                newInvestmentStatement.
                return newInvestmentStatement;
            }else{

            }
        }
        return null;
    }
}
