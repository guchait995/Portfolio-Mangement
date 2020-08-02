package com.example.portfoliomanagement.util;

import android.os.Build;
import android.text.format.DateFormat;

import com.example.portfoliomanagement.model.InvestmentStatement;

import java.util.List;

public class FinanceCalculations {
    public static double monthlyProfit(List<InvestmentStatement> investmentStatements){
        if(!investmentStatements.isEmpty())
            return investmentStatements.get(0).getUnreliesedGains()
                    - investmentStatements.get(investmentStatements.size()-1).getUnreliesedGains()
                    - investmentStatements.get(investmentStatements.size()-1).getTodaysGain();
        else
            return 0;
    }

    public static double netWorthGain(List<InvestmentStatement> investmentStatements){
        if(!investmentStatements.isEmpty())
            return investmentStatements.get(0).getCurrentNetWorth() - investmentStatements.get(investmentStatements.size()-1).getCurrentNetWorth();
        else
            return 0;
    }
    public static double monthlyInvestment(List<InvestmentStatement> investmentStatements) {
        if(!investmentStatements.isEmpty())
        return  investmentStatements.get(0).getInvestmentTillDate() - investmentStatements.get(investmentStatements.size()-1).getInvestmentTillDate();
        else
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
