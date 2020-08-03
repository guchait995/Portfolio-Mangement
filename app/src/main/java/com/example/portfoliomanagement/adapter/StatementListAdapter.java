package com.example.portfoliomanagement.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfoliomanagement.MainActivity;
import com.example.portfoliomanagement.R;
import com.example.portfoliomanagement.model.InvestmentStatement;
import com.example.portfoliomanagement.util.Utils;

import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

public class StatementListAdapter extends RecyclerView.Adapter<StatementListAdapter.StatementViewHolder> {
    List<InvestmentStatement> investmentStatements;
    private Context context;

    public StatementListAdapter(Context context, List<InvestmentStatement> investmentStatements) {
        List<InvestmentStatement> investmentStatementsTemp = new LinkedList<>();
        investmentStatementsTemp.add(new InvestmentStatement());
        investmentStatementsTemp.addAll(investmentStatements);
        this.investmentStatements = investmentStatementsTemp;
        this.context = context;
    }


    @NonNull
    @Override
    public StatementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.each_statement_layout, parent, false);
        StatementViewHolder statementViewHolder = new StatementViewHolder(v);
        return statementViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatementViewHolder holder, int position) {
        InvestmentStatement investmentStatement = investmentStatements.get(position);
        Date date = investmentStatement.getInvestmentDate();
        if (date == null && position == 0) {
            //First Row Headers
            holder.eachStatementDate.setText("Date");
            holder.eachStatementDate.setTypeface(null, Typeface.BOLD);
            holder.eachStatementCurrentNetworth.setText("Networth");
            holder.eachStatementCurrentNetworth.setTypeface(null, Typeface.BOLD);
            holder.eachStatementDaysGain.setText("T.Gains");
            holder.eachStatementDaysGain.setTypeface(null, Typeface.BOLD);
            holder.eachStatementDaysInvestment.setText("T.Inv.");
            holder.eachStatementDaysInvestment.setTypeface(null, Typeface.BOLD);
            holder.eachStatementUnrelisedGain.setText("U.Gains");
            holder.eachStatementUnrelisedGain.setTypeface(null, Typeface.BOLD);
            holder.deleteStatementButton.setVisibility(View.INVISIBLE);
        } else {
            holder.eachStatementDate.setTypeface(null, Typeface.NORMAL);
            holder.eachStatementCurrentNetworth.setTypeface(null, Typeface.NORMAL);
            holder.eachStatementDaysGain.setTypeface(null, Typeface.NORMAL);
            holder.eachStatementDaysInvestment.setTypeface(null, Typeface.NORMAL);
            holder.eachStatementUnrelisedGain.setTypeface(null, Typeface.NORMAL);
            holder.deleteStatementButton.setVisibility(View.VISIBLE);
            holder.eachStatementDate.setText(DateFormat.format(Utils.getShortAppDateFormat(), date).toString());
            holder.eachStatementCurrentNetworth.setText(
                    String.valueOf(investmentStatement.getCurrentNetWorth()));
            holder.eachStatementDaysGain.setText(
                    String.valueOf(investmentStatement.getTodaysGain()));
            holder.eachStatementDaysInvestment.setText(String.valueOf(investmentStatement.getInvestmentTillDate()));
            holder.eachStatementUnrelisedGain.setText(String.valueOf(investmentStatement.getUnreliesedGains()));
            holder.deleteStatementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.isConfirmedToDelete(context, investmentStatement);
                }
            });
            holder.eachStatementBody.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    holder.eachStatementBody.setBackgroundColor(context.getResources().getColor(R.color.white));
                    ((MainActivity) context).editInvestmentStatement(v, investmentStatement);
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return investmentStatements.size();
    }

    public static class StatementViewHolder extends RecyclerView.ViewHolder {
        TextView eachStatementDate;
        TextView eachStatementCurrentNetworth;
        TextView eachStatementDaysGain;
        TextView eachStatementDaysInvestment;
        TextView eachStatementUnrelisedGain;
        ImageView deleteStatementButton;
        LinearLayout eachStatementBody;

        public StatementViewHolder(@NonNull View itemView) {
            super(itemView);
            eachStatementDate = itemView.findViewById(R.id.each_statement_date);
            eachStatementCurrentNetworth = itemView.findViewById(R.id.each_statement_current_networth);
            eachStatementDaysGain = itemView.findViewById(R.id.each_statement_days_gain);
            eachStatementDaysInvestment = itemView.findViewById(R.id.each_statement_days_inevestment);
            eachStatementUnrelisedGain = itemView.findViewById(R.id.each_statement_days_unrelised_gain);
            deleteStatementButton = itemView.findViewById(R.id.delete_statement);
            eachStatementBody = itemView.findViewById(R.id.each_statement_body);
        }
    }
}
