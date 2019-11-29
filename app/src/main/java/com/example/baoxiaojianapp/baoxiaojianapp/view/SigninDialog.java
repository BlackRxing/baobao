package com.example.baoxiaojianapp.baoxiaojianapp.view;

import android.app.Dialog;
import android.content.Context;

import com.example.baoxiaojianapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SigninDialog extends Dialog {


    public SigninDialog(@NonNull Context context) {
        super(context);
        this.setContentView(R.layout.signindialog_layout);
    }

    public SigninDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.setContentView(R.layout.signindialog_layout);

    }

    protected SigninDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.setContentView(R.layout.signindialog_layout);

    }
}
