package com.example.portfoliomanagement.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.portfoliomanagement.R;

public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private EditText et1;
        private EditText et2;
        private EditText et3;
        private EditText et4;
        public GenericTextWatcher(View view,View layout)
        {
            this.view = view;
            et1= layout.findViewById(R.id.et_passcode1);
            et2= layout.findViewById(R.id.et_passcode2);
            et3= layout.findViewById(R.id.et_passcode3);
            et4= layout.findViewById(R.id.et_passcode4);
        }


        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            addTextWatch(text);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            Log.d("GenericTextWatcher",arg0.toString());
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        public void addTextWatch(String text){
            switch(view.getId())
            {
                case R.id.et_passcode1:
                    if(text.length()==1)
                        et2.requestFocus();
                    break;
                case R.id.et_passcode2:
                    if(text.length()==1)
                        et3.requestFocus();
                    else if(text.length()==0)
                        et1.requestFocus();
                    break;
                case R.id.et_passcode3:
                    if(text.length()==1)
                        et4.requestFocus();
                    else if(text.length()==0)
                        et2.requestFocus();
                    break;
                case R.id.et_passcode4:
                    if(text.length()==0)
                        et3.requestFocus();
                    break;
            }
        }
    }