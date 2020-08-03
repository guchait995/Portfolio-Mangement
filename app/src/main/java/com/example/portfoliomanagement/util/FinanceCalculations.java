package com.example.portfoliomanagement.util;

import android.os.Build;
import android.text.format.DateFormat;

import com.example.portfoliomanagement.model.InvestmentStatement;

import java.util.List;

public class FinanceCalculations {
    public static double monthlyProfit(List<InvestmentStatement> investmentStatements){
        /**
         * last_statement_unrelised_gain - (first_statement_unrelised_gains - first_statment_todays_gain)
         * **/
        if(!investmentStatements.isEmpty())
            return gainsAsOfLast(investmentStatements) - gainsAtOfStart(investmentStatements);
        else
            return 0;
    }
    public static double netWorthGain(List<InvestmentStatement> investmentStatements){
        /**last_statement_networth - (first_statement_networth - first_days_gain - first_days_investment)
           @Networth  of day includes that days investment as
           well as gains so we substract to get previous networth**/
        if(!investmentStatements.isEmpty())
            return networthAsOfLast(investmentStatements) - networthAsOfStart(investmentStatements);
        else
            return 0;
    }
    public static double monthlyInvestment(List<InvestmentStatement> investmentStatements) {
        /**last_investment_of_month - (investment_till_first_date - first_days_investment)
        @investment_till_first_date : includes that days investment**/
        if(!investmentStatements.isEmpty())
        return  investmentAsOfLast(investmentStatements) - investmentAsOfStart(investmentStatements);
        else
            return 0;

    }
    public static double networthAsOfStart(List<InvestmentStatement> investmentStatements) {
        /**
         @Networth  of day includes that days investment as
         well as gains so we substract to get previous networth**/
        if(!investmentStatements.isEmpty())
            return investmentStatements.get(investmentStatements.size()-1).getCurrentNetWorth()
                    -investmentStatements.get(investmentStatements.size()-1).getTodaysGain()
                    -investmentStatements.get(investmentStatements.size()-1).getTodayInvestment();
        return 0;
    }
    public static double gainsAtOfStart(List<InvestmentStatement> investmentStatements){
        /**
         *@Unrelised_Gain includes that days gain,
         * so first statement u.gains- days_gain would give us u.gain before first day
         * **/
        if(!investmentStatements.isEmpty())
            return investmentStatements.get(investmentStatements.size()-1).getUnreliesedGains()
                    - investmentStatements.get(investmentStatements.size()-1).getTodaysGain();
        return 0;
    }
    public static double investmentAsOfStart(List<InvestmentStatement> investmentStatements){
        if(!investmentStatements.isEmpty())
            return investmentStatements.get(investmentStatements.size()-1).getInvestmentTillDate()
                    - investmentStatements.get(investmentStatements.size()-1).getTodayInvestment() ;
        return 0;
    }
    public static double networthAsOfLast(List<InvestmentStatement> investmentStatements){
        if(!investmentStatements.isEmpty())
            return investmentStatements.get(0).getCurrentNetWorth();
        return 0;
    }
    public static double gainsAsOfLast(List<InvestmentStatement> investmentStatements){
        if(!investmentStatements.isEmpty())
            return investmentStatements.get(0).getUnreliesedGains();
        return 0;
    }
    public static double investmentAsOfLast(List<InvestmentStatement> investmentStatements){
        if(!investmentStatements.isEmpty())
            return investmentStatements.get(0).getInvestmentTillDate();
        return 0;
    }


}
