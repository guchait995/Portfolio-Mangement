package com.example.portfoliomanagement.model;


import android.os.Build;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.portfoliomanagement.util.Utils;

@Entity(tableName = "password_record")
public class PasswordRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "application")
    private String Application;

    @ColumnInfo(name = "password_type")
    private String PasswordType;

    @ColumnInfo(name = "password")
    private String password;


    public PasswordRecord() {
    }

    public PasswordRecord(int id, String application, String passwordType, String password) {
        this.id = id;
        Application = application;
        PasswordType = passwordType;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getApplication() {
        return Application;
    }

    public void setApplication(String application) {
        Application = application;
    }

    public String getPasswordType() {
        return PasswordType;
    }

    public void setPasswordType(String passwordType) {
        PasswordType = passwordType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean isInstanceOf(String passwordRecordString) {
        return PasswordRecord.class.getName().equals(passwordRecordString.split(",")[0]);
    }

    public static PasswordRecord fromString(String passwordRecordString) {
        String[] passwordRecordStrArr = passwordRecordString.split(",");
        int id = Integer.parseInt(passwordRecordStrArr[1]);
        String application = passwordRecordStrArr[2];
        String passwordType = passwordRecordStrArr[3];
        String password = passwordRecordStrArr[4];
        return new PasswordRecord(id, application, passwordType, password);
    }

    public String getBackupRecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join(",",
                    this.getClass().getName(),
                    String.valueOf(this.id),
                    this.Application,
                    this.PasswordType,
                    this.password
            );
        }
        return null;
    }
}
