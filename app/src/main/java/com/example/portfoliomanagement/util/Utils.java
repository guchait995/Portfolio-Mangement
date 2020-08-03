package com.example.portfoliomanagement.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.DatePicker;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.portfoliomanagement.MainActivity;
import com.example.portfoliomanagement.PasswordListActivity;
import com.example.portfoliomanagement.R;
import com.example.portfoliomanagement.model.InvestmentStatement;
import com.example.portfoliomanagement.model.PasswordRecord;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utils {


    public static final String APP_DATE_FORMAT = "%s/%s/%s";
    public static final String SHORT_APP_DATE_FORMAT = "%s/%s";
    public static final int PICKFILE_RESULT_CODE = 9998;

    public static Date stringToDate(String aDate, String aFormat) {

        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    public static Map<Date, InvestmentStatement> investmentStatementDateListToMap(List<InvestmentStatement> investmentStatements) {
        Map<Date, InvestmentStatement> dateInvestmentStatementMap = new HashMap<>();
        for (InvestmentStatement investmentStatement : investmentStatements) {
            dateInvestmentStatementMap.put(investmentStatement.getInvestmentDate(), investmentStatement);
        }
        return dateInvestmentStatementMap;
    }

    public static InvestmentStatement createInvestmentStatment(InvestmentStatement lastInvestment, InvestmentStatement newStatement) {
        if (lastInvestment != null)
            return newStatement;
        return newStatement;
    }

    public static String getAppDateFormat() {
        return String.format(APP_DATE_FORMAT, "dd", "MM", "yyyy");
    }

    public static String getShortAppDateFormat() {
        return String.format(SHORT_APP_DATE_FORMAT, "dd", "MM");
    }


    public static Date getDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        if (year != 0) {
            calendar.set(year, month, dayOfMonth);
        }
        return calendar.getTime();
    }

    public static Date getEndDateOfTheMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        if (year != 0) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month+1);//next month
            calendar.set(Calendar.DATE,0); // gives last day of previous month
        }
        return calendar.getTime();
    }

    public static Date getStartDateOfTheMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        if (year != 0) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,1);
        }
        calendar.add(Calendar.DATE,-1);
        return calendar.getTime();
    }

    public static String dateToString(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        if (year != 0) {
            calendar.set(year, month, dayOfMonth);
        }

        SimpleDateFormat dayFormat = new SimpleDateFormat(getAppDateFormat(), Locale.US);
        return dayFormat.format(calendar.getTime());
    }

    public static String getTodaysDateString() {
        return dateToString(0, 0, 0);
    }

    public static String getStringFromDate(Date date) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(getAppDateFormat());
        return simpledateformat.format(date);
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void isConfirmedToDelete(Context context, Object o) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Ok, Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (o instanceof PasswordRecord) {
                    ((PasswordListActivity) context).deletePasswordRecords((PasswordRecord) o);
                }
                if (o instanceof InvestmentStatement) {
                    ((MainActivity) context).deleteInvestment((InvestmentStatement) o);
                }

            }
        });
        builder.setNegativeButton("No, Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static boolean checkPermissions(Context context) {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(((Activity) context),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    public static Date getDateFromString(String dateStr) {
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat(getAppDateFormat());
            return simpledateformat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    public static int positionInYearArray(Context context, int value) {
        String[] array = context.getResources().getStringArray(R.array.yearItems);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(String.valueOf(value)))
                return i;
        }
        return 0;
    }
}
