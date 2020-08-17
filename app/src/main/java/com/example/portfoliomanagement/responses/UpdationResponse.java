package com.example.portfoliomanagement.responses;

import android.os.Build;

import com.example.portfoliomanagement.model.UserContactDetail;

import java.util.LinkedList;
import java.util.List;
public class UpdationResponse {

    public enum Status{
        PENDING_VERIFICATION,
        VERIFICATION_SUCCESSFULL,
        SUCCESS,
        FAILURE
    }
    private List<UserContactDetail> hasToUpdated;
    private List<UserContactDetail> hasToDeleted;
    private Status status;


    public UpdationResponse() {
        hasToDeleted=new LinkedList<>();
        hasToUpdated=new LinkedList<>();
    }

    public List<UserContactDetail> getHasToUpdated() {
        return hasToUpdated;
    }

    public List<UserContactDetail> getHasToDeleted() {
        return hasToDeleted;
    }
    public boolean addContactDetailsHasToDeleted(UserContactDetail userContactDetail){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            UserContactDetail existing=hasToDeleted.stream()
//                    .filter(ucd->
//                      ucd.id==userContactDetail.id
//                    )
//                    .findFirst()
//                    .orElse(null);
//            if(existing==null){
//                return hasToDeleted.add(userContactDetail);
//            }
//        }else{
            if(!hasToDeleted.contains(userContactDetail)){
                return hasToDeleted.add(userContactDetail);

            }
       return false;
    }
    public boolean addContactDetailsHasToUpdated(UserContactDetail userContactDetail){
        if(!hasToUpdated.contains(userContactDetail)){
            return hasToUpdated.add(userContactDetail);
        }
        else return false;
    }

}
