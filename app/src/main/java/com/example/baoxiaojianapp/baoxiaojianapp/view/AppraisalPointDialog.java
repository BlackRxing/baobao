package com.example.baoxiaojianapp.baoxiaojianapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.baoxiaojianapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppraisalPointDialog extends Dialog {
    public AppraisalPointDialog(@NonNull Context context) {

        super(context);
        this.setContentView(R.layout.appraisalpointdialog_layout);
    }

    public AppraisalPointDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.setContentView(R.layout.appraisalpointdialog_layout);

    }

    protected AppraisalPointDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.setContentView(R.layout.appraisalpointdialog_layout);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
