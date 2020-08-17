package com.example.portfoliomanagement.model;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserContactDetail {
    public int id;
    public String name;
    public String emailId;;
    public String phoneNumber;
    public Uri uri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }


    public boolean isRedundentName(String name){
        return !this.name.equals(getNonRedundentName(name));
    }
    public String getNonRedundentName(String name){
        String[] names=name.split(" ");
        List<String> newName=new LinkedList<>();
        for(String n:names){
            if(!newName.contains(n)){
                newName.add(n);
            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join(" ",newName);
        }else{
            return this.name;
        }
    }

    public boolean isDuplicate(UserContactDetail userContactDetail){
        boolean same = true;
        if(this.id==userContactDetail.id) {
            //same not duplicate
            return false;
        }
        if(userContactDetail.getName().equals(this.name)){
            if(userContactDetail.getPhoneNumber()!=null){
                //phone present
                if(userContactDetail.getEmailId()!=null){
                    //email and phone both present
                    same = same && isNumberSame(userContactDetail.getPhoneNumber(),this.phoneNumber) && userContactDetail.getEmailId().equals(this.emailId);
                }else{
                    //phone present email absent
                    same= same && isNumberSame(userContactDetail.getPhoneNumber(),this.phoneNumber);

                }

            }else{
                //phone absent
                if(userContactDetail.getEmailId()!=null) {
                    same= same && userContactDetail.getEmailId().equals(this.emailId);
                 }
            }
        }else{
            same = same && false;
        }
        return same;
    }

    @NonNull
    @Override
    public String toString() {
        String s="Id: "+this.id+" Name: "+this.getName();
        if(this.getPhoneNumber()!=null){
            return s+" phone: "+this.getPhoneNumber();
        }
        if(this.getEmailId()!=null){
            return s+" phone: "+this.getEmailId();
        }
        return s;
    }

    private boolean isNumberSame(String phone1,String phone2){
        String newPhone1= phone1.replaceAll("[^0-9]", "");
        String newPhone2= phone2.replaceAll("[^0-9]", "");
        return newPhone1.equals(newPhone2);

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof UserContactDetail){
            return this.id==((UserContactDetail) obj).id;
        }
        return super.equals(obj);
    }
}
