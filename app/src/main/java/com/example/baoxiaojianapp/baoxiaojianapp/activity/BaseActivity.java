package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int getLayoutResId();


    protected void hideSoftKeyboard(){
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(getCurrentFocus()!=null){
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}
