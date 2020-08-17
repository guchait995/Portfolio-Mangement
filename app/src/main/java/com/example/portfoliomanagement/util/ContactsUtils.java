package com.example.portfoliomanagement.util;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.widget.Toast;

import com.example.portfoliomanagement.model.UserContactDetail;
import com.example.portfoliomanagement.responses.UpdationResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class ContactsUtils {
    public static final String TAG = "ContactsUtils-1995";
    private static ArrayList<ContentProviderOperation> operations = new ArrayList<>();
    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
    private static final String[] PROJECTION = {ContactsContract.Data.CONTACT_ID, ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.DATA1,ContactsContract.Data.DATA2, ContactsContract.Data.MIMETYPE,ContactsContract.Contacts.LOOKUP_KEY};
    private static final String ORDER = DISPLAY_NAME;
    private static final String selection = ContactsContract.Data.MIMETYPE + " = ?" +
            " OR " + ContactsContract.Data.MIMETYPE + " = ?" + " OR " + ContactsContract.Data.MIMETYPE + " = ?";
    private static final String[] selectionArgs = {"%" + "@" + "%", ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
    private static final List<Uri> uris=new LinkedList<>();
    public static final int ACTION_DELETE=0;
    public static final int ACTION_UPDATE=1;


    /***
     * @CHECK_THROUGH_ALL_CONTACT
     * */
    public static UpdationResponse checkAllContacts(Context context, boolean takeAction, int actionType) {
        UpdationResponse updationResponse=new UpdationResponse();
        LinkedHashMap<Integer, UserContactDetail> userContactDetailLinkedHashMap = new LinkedHashMap<>();
        List<UserContactDetail> userContactDetails=new LinkedList<>();
        operations=new ArrayList<>();
        try {
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Data.CONTENT_URI, PROJECTION, selection, selectionArgs, ORDER);
            if (cur != null && cur.getCount() > 0 && cur.moveToFirst()) {
                do {
                    int hasPhone = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int contactId = cur.getInt(cur.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY));
                    String emailOrMobile = cur.getString(cur.getColumnIndex(ContactsContract.Data.DATA1));
                    String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                    if (hasPhone > 0 && emailOrMobile != null) {
                        UserContactDetail contactDetail;
                        if (!userContactDetailLinkedHashMap.containsKey(contactId)) {
                            contactDetail = new UserContactDetail();
                            if (!Utils.isEmailValid(emailOrMobile)) {
                                contactDetail.setPhoneNumber(emailOrMobile);
                            } else {
                                contactDetail.setEmailId(emailOrMobile);
                            }
                            contactDetail.setName(name);
                            userContactDetailLinkedHashMap.put(contactId, contactDetail);
                        } else {
                            contactDetail = userContactDetailLinkedHashMap.get(contactId);
                            if (contactDetail == null) continue;
                            if (!Utils.isEmailValid(emailOrMobile)) {
                                contactDetail.setPhoneNumber(emailOrMobile);
                            } else {
                                contactDetail.setEmailId(emailOrMobile);
                            }
                            contactDetail.setName(name);
                            userContactDetailLinkedHashMap.put(contactId, contactDetail);
                        }
                        contactDetail.setUri(uri);
                        contactDetail.setId(contactId);
                        uris.add(uri);
                        Log.d(TAG,contactDetail.toString());

                        if(contactDetail.isRedundentName(name)){
                            //update contacts here
                            if(takeAction) {
                                //takeAction is true so we need to take the action
                                if(actionType==ACTION_UPDATE) {
                                    Log.d(TAG,"To Be Updated: "+contactDetail.toString());
                                    updateContact(contactDetail, name,context);
                                }
                            }else{
                                //takeAction is false so we need to just calculate the response
                                updationResponse.addContactDetailsHasToUpdated(contactDetail);
                            }
                        }
                        if(userContactDetails.size() > 0 && isContactDetailsExisting(contactDetail,userContactDetails)){
                            //delete contacts here
                            Log.d(TAG,"To Be Delete: "+contactDetail.toString());
                            if(takeAction){
                                //takeAction is true so we need to take the action
                                if(actionType==ACTION_DELETE){
                                    deleteContact(contactId,context);
                                }
                            }else{
                                //takeAction is false so we need to just calculate the response
                                updationResponse.addContactDetailsHasToDeleted(contactDetail);
                            }
                        }
                        userContactDetails.add(contactDetail);

                    }
                } while (cur.moveToNext());

            }
            if (cur != null) {
                cur.close();
            }
            if(takeAction){
                if(actionType==ACTION_UPDATE){
                    batchUpdate(context);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return updationResponse;
    }
    /***
     * @DELETE_CONTACT
     * */
    private static boolean deleteContact(int contactId,Context context){
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,String.valueOf(contactId));
        int deleted = context.getContentResolver().delete(uri,null,null);
        return deleted>0;
    }

    /***
     * @UPDATE_CONTACT
     * */
    private static boolean updateContact(UserContactDetail contactDetail,String name,Context context){
        Log.d(TAG,contactDetail.getName()+" -> "+contactDetail.getNonRedundentName(contactDetail.getName()));
        contactDetail.setName(contactDetail.getNonRedundentName(name));
        String where = String.format(
                "%s = '%s' AND %s = ?",
                ContactsContract.Data.MIMETYPE, //mimetype
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                ContactsContract.Data.CONTACT_ID/*contactId*/);
        String[] args = {String.valueOf(contactDetail.getId())};
        Log.d(TAG,"Update with ID: "+args);
        return operations.add(
                ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, args)
                        .withValue(ContactsContract.Data.DISPLAY_NAME_PRIMARY, contactDetail.getName())
                        .build()
        );

    }

    private static boolean createContact(UserContactDetail userContactDetail,Context context){

        return operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.CONTACT_ID,userContactDetail.getId())
                        .withValue(ContactsContract.Data.DISPLAY_NAME_PRIMARY, userContactDetail.getName())
                        .withValue(ContactsContract.Data.DATA1,userContactDetail.getPhoneNumber())
                        .build()
        );
    }


    private static boolean isContactDetailsExisting(UserContactDetail userContactDetail, List<UserContactDetail> userContactDetails){
        for(UserContactDetail ucd:userContactDetails){
            if(userContactDetail.isDuplicate(ucd)){
               return true;
            }
        }
        return false;
    }

    /***
     * @BATCH_UPDATE_CONTACT
     * */
    private static boolean batchUpdate(Context context){
        try {

            ContentProviderResult[] results = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
            for (ContentProviderResult result : results) {
                Log.d(TAG, "Result: "+result.toString());
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
