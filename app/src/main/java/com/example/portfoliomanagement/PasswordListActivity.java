package com.example.portfoliomanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portfoliomanagement.model.PasswordRecord;
import com.example.portfoliomanagement.service.PasswordService;
import com.example.portfoliomanagement.util.GenericTextWatcher;

public class PasswordListActivity extends AppCompatActivity {

    private RecyclerView rvPasswordRecord;
    private PasswordService passwordService;
    private Button btnAddPasswordRecordPopup;
    private String passcode="";
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);
        openPasswordPopup(this);
        rvPasswordRecord=findViewById(R.id.rv_password_list);
        rvPasswordRecord.setLayoutManager(new LinearLayoutManager(this));
        passwordService=new PasswordService(this);
        btnAddPasswordRecordPopup=findViewById(R.id.add_password_pop_up);
        btnAddPasswordRecordPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPasswordRecordAddPopup(null);
            }
        });

    }





    public void openPasswordRecordAddPopup(PasswordRecord passwordRecord){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.add_password_record, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        final EditText etApplication=customLayout.findViewById(R.id.et_application);
        final EditText etPasswordType=customLayout.findViewById(R.id.et_password_type);
        final EditText etPassword=customLayout.findViewById(R.id.et_password);
        final EditText etPasswordConfirm=customLayout.findViewById(R.id.et_password_confirm);
        final Button btnPasswordRecordSave=customLayout.findViewById(R.id.btn_password_record_save);
        if(passwordRecord!=null){
            etApplication.setText(passwordRecord.getApplication());
            etPasswordType.setText(passwordRecord.getPasswordType());
            etPasswordConfirm.setText(passwordRecord.getPassword());
            etPassword.setText(passwordRecord.getPassword());
        }else{
            passwordRecord=new PasswordRecord();
        }
        final PasswordRecord passwordRecordTemp=passwordRecord;
        btnPasswordRecordSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String applicationName=etApplication.getText().toString();
                String passwordType=etPasswordType.getText().toString();
                String password=etPassword.getText().toString();
                String confirmPassword =etPasswordConfirm.getText().toString();
                if(password.equals(confirmPassword)){
                    passwordRecordTemp.setApplication(applicationName);
                    passwordRecordTemp.setPassword(password);
                    passwordRecordTemp.setPasswordType(passwordType);
                    savePasswordRecord(passwordRecordTemp);
                    dialog.dismiss();
                    refreshPasswordRecords();
                }else{
                    Toast.makeText(getApplicationContext(),"Password Mismatch !!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    public  void  editPasswordRecord(PasswordRecord passwordRecord){
        openPasswordRecordAddPopup(passwordRecord);
    }
    public void refreshPasswordRecords(){
        passwordService.fetchPassword(rvPasswordRecord);
    }
    public void deletePasswordRecords(PasswordRecord passwordRecord){
        passwordService.deletePassword(passwordRecord,rvPasswordRecord);
    }
    public void savePasswordRecord(PasswordRecord passwordRecord){
        passwordService.addPasswordRecord(passwordRecord);
    }
    public void openPasswordPopup(Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final View customLayout = getLayoutInflater().inflate(R.layout.pin_layout, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        final TextView passcodeCancel=customLayout.findViewById(R.id.passcode_cancel);
        final Button passcodeVerify=customLayout.findViewById(R.id.passcode_verify);
        initPasscodePinLayoutBehaviour(customLayout);
        passcodeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back click
                ((Activity)context).finish();
            }
        });
        passcodeVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check passcode
                passcode=et1.getText().toString()+
                        et2.getText().toString()+
                        et3.getText().toString()+
                        et4.getText().toString();
                if(verifyPasscode(passcode)){
                    //correct passcode
                    refreshPasswordRecords();
                    dialog.dismiss();
                }else{
                    //inorrect passcode reset
//                    passcode.setText("");
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");

                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
    public void initPasscodePinLayoutBehaviour(View layout){
//        String passcode;
        et1= layout.findViewById(R.id.et_passcode1);
        et2= layout.findViewById(R.id.et_passcode2);
        et3= layout.findViewById(R.id.et_passcode3);
        et4= layout.findViewById(R.id.et_passcode4);
        et1.addTextChangedListener(new GenericTextWatcher(et1,layout));
        et2.addTextChangedListener(new GenericTextWatcher(et2,layout));
        et3.addTextChangedListener(new GenericTextWatcher(et3,layout));
        et4.addTextChangedListener(new GenericTextWatcher(et4,layout));
    }
    public boolean verifyPasscode(String passcodeStr) {
        if (passcodeStr.length() == 4) {
            int passcode = Integer.parseInt(passcodeStr);
            if (passcode == 6884) {
                Toast.makeText(this, "Passcode Verification Successfull!!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "Passcode Error!!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Enter valid passcode", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
